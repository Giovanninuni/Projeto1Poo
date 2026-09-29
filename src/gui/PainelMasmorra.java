package gui;

import javax.swing.JPanel;

import contratos.ResultadoAcao;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import entidades.Heroi;
import entidades.SpriteMapa;
import equipamentos.DepositoEquipamentos;
import equipamentos.Equipamento;
import mundo.Mapa;
import mundo.Masmorra;
import mundo.TilesetMasmorra;
import objetos.Bau;
import objetos.Mercador;

public class PainelMasmorra extends JPanel implements KeyListener {

    private JanelaPrincipal janela;
    private Masmorra masmorra;

    private final int TAMANHO_TILE = 32;

    // Zoom 2x: cada tile de 32 px aparece com 64 px na tela (16x12 tiles
    // visíveis numa janela 1024x768), e a câmera acompanha o herói.
    private final Camera camera = new Camera(2);

    // Velocidade de caminhada, em pixels do mapa por segundo (160 = 5 tiles
    // por segundo, a mesma do movimento antigo em passos de 200 ms).
    private static final double VELOCIDADE_PX_POR_SEGUNDO = 160;

    // Maior "dt" aceito num quadro. Se o jogo parar um instante (ex: um
    // diálogo aberto), o quadro seguinte não teleporta o herói pra longe.
    private static final double DT_MAXIMO_SEGUNDOS = 0.05;

    // Velocidade das animações: cada quadro fica na tela por esse tanto de ms.
    private static final long MS_POR_QUADRO_PARADO = 180;
    private static final long MS_POR_QUADRO_ANDANDO = 100;

    // Personagens são desenhados 1,5x maiores que o tile (48 px em vez de
    // 32): o desenho da Momonga ocupa só o miolo da célula e parecia pequeno
    // e "longe" dos baús e paredes. Com o zoom 2x da câmera, 1,5 x 2 = 3:
    // cada pixel do sprite vira exatamente 3 pixels na tela, então continua
    // nítido (um zoom quebrado na tela é que entortaria o pixel art).
    private static final int TAMANHO_PERSONAGEM = 48;

    /** Um personagem pronto pra desenhar: qual quadro e onde ficam os pés dele (em pixels do mapa). */
    private record Figura(BufferedImage imagem, double pesX, double pesY, Color corReserva) {}

    // Game loop: ~60 vezes por segundo lê o teclado, move o herói e redesenha.
    private final Timer relogio = new Timer(16, e -> aCadaQuadro());
    private long ultimoQuadroNs = System.nanoTime();

    // Quais teclas estão apertadas agora (ver Teclado).
    private final Teclado teclado = new Teclado();

    private boolean andando = false;
    private boolean olhandoParaEsquerda = false;

    public PainelMasmorra(JanelaPrincipal janela, Masmorra masmorra) {
        this.janela = janela;
        this.masmorra = masmorra;
        setBackground(Color.BLACK);
        setFocusable(true);

        // Dois "ouvintes" de teclado com papéis diferentes: o Teclado só
        // anota o que está apertado (movimento contínuo, lido a cada quadro);
        // o próprio painel trata as teclas de ação (E, I, P), que acontecem
        // uma vez por aperto.
        addKeyListener(teclado);
        addKeyListener(this);

        // Se outra coisa pegar o foco (diálogo do baú, tela de combate...),
        // o painel não fica sabendo quando a tecla é solta. Então, ao perder
        // o foco, esquece as teclas — senão o herói andaria sozinho.
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                teclado.soltarTodas();
            }
        });
    }

    // addNotify/removeNotify: o Swing chama quando o painel entra e sai da
    // janela. Assim o relógio só roda enquanto o painel existe na tela — ao
    // recomeçar a partida, o painel antigo é removido e o relógio dele para.
    @Override
    public void addNotify() {
        super.addNotify();
        ultimoQuadroNs = System.nanoTime();
        relogio.start();
    }

    @Override
    public void removeNotify() {
        relogio.stop();
        super.removeNotify();
    }

    /**
     * Um quadro do game loop, chamado pelo relógio a cada ~16 ms:
     * 1) mede quanto tempo passou desde o quadro anterior (dt, em segundos);
     * 2) atualiza o jogo (move o herói conforme as teclas apertadas agora);
     * 3) redesenha a tela.
     * Mover por "velocidade x dt" deixa a velocidade certa mesmo se algum
     * quadro atrasar: um quadro mais longo anda proporcionalmente mais.
     */
    private void aCadaQuadro() {
        long agora = System.nanoTime();
        double dt = Math.min((agora - ultimoQuadroNs) / 1_000_000_000.0, DT_MAXIMO_SEGUNDOS);
        ultimoQuadroNs = agora;

        atualizar(dt);
        repaint();
    }

    private void atualizar(double dt) {
        // Direção a partir das teclas apertadas neste instante. Cada tecla
        // soma -1 ou +1 num eixo; teclas opostas se anulam (A + D = parado).
        int direcaoX = 0;
        int direcaoY = 0;
        if (teclado.algumaApertada(KeyEvent.VK_A, KeyEvent.VK_LEFT)) direcaoX -= 1;
        if (teclado.algumaApertada(KeyEvent.VK_D, KeyEvent.VK_RIGHT)) direcaoX += 1;
        if (teclado.algumaApertada(KeyEvent.VK_W, KeyEvent.VK_UP)) direcaoY -= 1;
        if (teclado.algumaApertada(KeyEvent.VK_S, KeyEvent.VK_DOWN)) direcaoY += 1;

        andando = false;
        if (direcaoX == 0 && direcaoY == 0) {
            return;
        }

        if (direcaoX < 0) {
            olhandoParaEsquerda = true;
        } else if (direcaoX > 0) {
            olhandoParaEsquerda = false;
        }

        // Normalização do vetor: (1, 1) tem comprimento raiz de 2 (1,41),
        // então sem isso a diagonal andaria 41% mais rápido que a reta.
        // Dividindo cada eixo pelo comprimento, qualquer direção vira um
        // vetor de tamanho 1, e a velocidade fica igual pra todo lado.
        double comprimento = Math.hypot(direcaoX, direcaoY);
        double distancia = VELOCIDADE_PX_POR_SEGUNDO * dt;

        int tileXAntes = masmorra.getHeroiX();
        int tileYAntes = masmorra.getHeroiY();
        andando = masmorra.moverHeroi(direcaoX / comprimento * distancia, direcaoY / comprimento * distancia);

        // A batalha começa quando o herói ENTRA no tile do encontro. Checar
        // só na troca de tile mantém a correção antiga: depois de fugir,
        // andar dentro do tile do encontro não reabre a batalha.
        boolean trocouDeTile = masmorra.getHeroiX() != tileXAntes || masmorra.getHeroiY() != tileYAntes;
        if (trocouDeTile) {
            Masmorra.Encontro encontro = masmorra.getEncontroNaPosicaoDoHeroi();
            if (encontro != null) {
                teclado.soltarTodas();
                andando = false;
                janela.iniciarCombate(encontro);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        long agora = System.currentTimeMillis();

        // A câmera mira o herói (meio tile acima dos pés, mais ou menos o
        // centro do corpo).
        Mapa mapa = masmorra.getMapa();
        camera.seguir(
                masmorra.getHeroiPx(),
                masmorra.getHeroiPy() - TAMANHO_TILE / 2.0,
                mapa.getLargura() * TAMANHO_TILE,
                mapa.getAltura() * TAMANHO_TILE,
                getWidth(),
                getHeight());

        // Trabalha numa cópia do Graphics pra o zoom não "vazar" pro resto
        // do Swing. Vizinho mais próximo: ao ampliar, cada pixel vira um
        // quadradinho nítido, em vez de ficar borrado.
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        camera.aplicar(g2);
        desenharCena(g2, agora);
        g2.dispose();
    }

    /**
     * Desenha tudo em pixels do mapa (tile = 32), como se não houvesse
     * câmera — o zoom e o deslocamento já foram aplicados no Graphics.
     */
    private void desenharCena(Graphics g, long agora) {
        // Ordem importa: o que é desenhado depois fica por cima.
        desenharMapa(g);

        //baus fechados e abertos com sprites diferentes -- Lalae
        for (Bau i: masmorra.getBaus()) {
            int id = i.isFechado() ? TilesetMasmorra.BAU_FECHADO : TilesetMasmorra.BAU_VAZIO;
            desenharImagem(g, tileDoTileset(id), i.getX(), i.getY(), Color.YELLOW);
        }

        // Personagens: primeiro junta todos numa lista, depois desenha de
        // cima pra baixo na tela (ordem de y). Como o desenho é mais alto que
        // o tile, quem está mais embaixo tem que ficar na frente — senão a
        // cabeça de um monstro logo abaixo apareceria atrás dos pés do herói.
        List<Figura> figuras = new ArrayList<>();

        // Cada encontro que ainda não foi derrotado aparece, parado e
        // "respirando", com o desenho do primeiro monstro do grupo
        // (ex: 3 goblins -> um goblin no mapa).
        for (Masmorra.Encontro encontro : masmorra.getEncontros()) {
            if (!encontro.isDerrotado()) {
                SpriteMapa sprite = encontro.getInimigos().get(0).getSpriteMapa();
                figuras.add(figuraParada(sprite, encontro.getX(), encontro.getY(), agora, Color.RED));
            }
        }

        for (Mercador mercador : masmorra.getMercadores()) {
            figuras.add(figuraParada(mercador.getSpriteMapa(), mercador.getX(), mercador.getY(), agora, Color.GREEN));
        }

        // O grupo aparece no mapa com o desenho do primeiro herói. Ele entra
        // por último na lista: a ordenação mantém a ordem de quem tem o mesmo
        // y, então parado em cima de um encontro (depois de fugir) o herói
        // fica na frente do monstro.
        SpriteMapa spriteHeroi = janela.getGrupo().getHerois().get(0).getSpriteMapa();
        long msPorQuadro = andando ? MS_POR_QUADRO_ANDANDO : MS_POR_QUADRO_PARADO;
        BufferedImage quadroHeroi = CarregadorSprites.recortarQuadro(
                spriteHeroi, andando, quadroAtual(agora, msPorQuadro, 0), olhandoParaEsquerda);
        figuras.add(new Figura(quadroHeroi, masmorra.getHeroiPx(), masmorra.getHeroiPy(), Color.BLUE));

        figuras.sort(Comparator.comparingDouble(Figura::pesY));
        for (Figura figura : figuras) {
            desenharPersonagem(g, figura);
        }
    }

    /** Monta a figura de um personagem parado (animação de "respirar") no tile (x, y). */
    private Figura figuraParada(SpriteMapa sprite, int x, int y, long agora, Color corReserva) {
        // A defasagem depende da posição: assim cada personagem respira no
        // seu ritmo, em vez de todos mexerem exatamente ao mesmo tempo.
        int quadro = quadroAtual(agora, MS_POR_QUADRO_PARADO, x * 3 + y);
        BufferedImage imagem = CarregadorSprites.recortarQuadro(sprite, false, quadro, false);
        // Pés no meio do fundo do tile
        return new Figura(imagem, x * TAMANHO_TILE + TAMANHO_TILE / 2.0, (y + 1) * TAMANHO_TILE, corReserva);
    }

    /**
     * Desenha um personagem ancorado pelos pés: o meio do fundo do desenho
     * fica no ponto dos pés, e ele cresce pra cima (a cabeça entra no tile
     * de cima, como nos RPGs em visão 3/4).
     */
    private void desenharPersonagem(Graphics g, Figura figura) {
        if (figura.imagem() == null) {
            g.setColor(figura.corReserva());
            g.fillRect((int) figura.pesX() - TAMANHO_TILE / 2, (int) figura.pesY() - TAMANHO_TILE,
                    TAMANHO_TILE, TAMANHO_TILE);
            return;
        }

        double esquerda = camera.alinharAoPixelDaTela(figura.pesX() - TAMANHO_PERSONAGEM / 2.0);
        double topo = camera.alinharAoPixelDaTela(figura.pesY() - TAMANHO_PERSONAGEM);

        // O drawImage só aceita posição inteira, mas o translate aceita número
        // quebrado: assim o herói pode ficar em meio pixel do mapa (= 1 pixel
        // da tela) e o movimento livre sai liso, sem "tremer".
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(esquerda, topo);
        g2.drawImage(figura.imagem(), 0, 0, TAMANHO_PERSONAGEM, TAMANHO_PERSONAGEM, null);
        g2.dispose();
    }

    /**
     * Qual dos quadros da animação mostrar agora. Usa o relógio: a cada
     * msPorQuadro milissegundos passa pro próximo, e depois do último volta
     * pro primeiro (por isso o %).
     */
    private int quadroAtual(long agora, long msPorQuadro, int defasagem) {
        return (int) ((agora / msPorQuadro + defasagem) % SpriteMapa.QUADROS);
    }

    /**
     * Desenha uma imagem na posição (x, y) do grid. Se a imagem não existir
     * (arquivo faltando), cai pro retângulo da corReserva; se corReserva
     * for null, simplesmente não desenha nada.
     */
    private void desenharImagem(Graphics g, BufferedImage imagem, int x, int y, Color corReserva) {
        int px = x * TAMANHO_TILE;
        int py = y * TAMANHO_TILE;
        if (imagem != null) {
            g.drawImage(imagem, px, py, TAMANHO_TILE, TAMANHO_TILE, null);
        } else if (corReserva != null) {
            g.setColor(corReserva);
            g.fillRect(px, py, TAMANHO_TILE, TAMANHO_TILE);
        }
    }

    /** Recorta o tile de número id da folha tileset_masmorra.png. */
    private BufferedImage tileDoTileset(int id) {
        return CarregadorSprites.recortarTile(TilesetMasmorra.ARQUIVO, id, TilesetMasmorra.COLUNAS, TAMANHO_TILE);
    }

    /**
     * Desenha cada tile do mapa em duas camadas: primeiro o chão/parede,
     * depois a decoração por cima (ex: ossos, que têm fundo transparente).
     * Se o tileset não existir em assets/sprites/tiles/, cai pro retângulo
     * da cor placeholder do TipoTile — assim o jogo roda mesmo sem a imagem.
     */
    private void desenharMapa(Graphics g) {
        Mapa mapa = masmorra.getMapa();

        for (int y = 0; y < mapa.getAltura(); y++) {
            for (int x = 0; x < mapa.getLargura(); x++) {
                int idVisual = mapa.getIdVisual(x, y);

                // Se a célula tem um tile específico do tileset, desenha esse
                // recorte. Senão, cai pro sprite único por TipoTile.
                BufferedImage sprite = (idVisual >= 0)
                        ? tileDoTileset(idVisual)
                        : CarregadorSprites.carregar(mapa.getTile(x, y).getArquivoSprite());
                desenharImagem(g, sprite, x, y, mapa.getTile(x, y).getCorPlaceholder());

                int idDecoracao = mapa.getIdDecoracao(x, y);
                if (idDecoracao >= 0) {
                    desenharImagem(g, tileDoTileset(idDecoracao), x, y, null);
                }
            }
        }
    }

    // Metodos abaixo da classe keyListener

    @Override
    public void keyPressed(KeyEvent e) {
        // As teclas de movimento não são tratadas aqui: o Teclado só anota o
        // que está apertado e o game loop (atualizar) move o herói. Aqui
        // ficam as ações que acontecem uma vez por aperto (P, E, I).
        int tecla = e.getKeyCode();

        //abre a tela de status apertando P
        if (tecla == KeyEvent.VK_P){
            janela.mostrarStatus();
            return;
        }

        //checagem para abrir baus apertando E -- Lalae
        if (tecla == KeyEvent.VK_E){
            Bau bauAdjacente = masmorra.getBauAdjacenteAoHeroi();
            Mercador mercadorAdjacente = masmorra.getMercadorAdjacenteAoHeroi();

            // Se o herói estiver ao lado de um baú E de um mercador ao mesmo
            // tempo, o baú tem prioridade (else if): o E faz uma coisa só.
            if(bauAdjacente != null){
                //escolhendo o heroi que vai pegar o item
                
                String[] opcoesHerois = janela.getGrupo().obterMenuDeHerois();
                
                String heroiEscolhido = (String) JOptionPane.showInputDialog(
                        this,
                        "Escolha um Heroi para receber o item: ",
                        "Bau Velho",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        opcoesHerois,
                        opcoesHerois[0]
                );
                
                if(heroiEscolhido != null){
                    int indice = Integer.parseInt(heroiEscolhido.split(" ")[0]);

                    ResultadoAcao abriuBau = bauAdjacente.abrir(janela.getGrupo().getHerois().get(indice - 1), janela.getGrupo().getDeposito());

                    JOptionPane.showMessageDialog(janela, abriuBau.getMensagem());
                }



            } else if (mercadorAdjacente != null) {
                // Provisório até a PainelLoja existir: depois vira
                // janela.mostrarLoja(mercadorAdjacente.getLoja());
                JOptionPane.showMessageDialog(janela, "Bem-vindo à " + mercadorAdjacente.getLoja().getNome() + "!");
            }
        }

        //abre o menu de equipar itens do deposito apertando I
        if (tecla == KeyEvent.VK_I){
            DepositoEquipamentos deposito = janela.getGrupo().getDeposito();

            if(deposito.estaVazio()){
                JOptionPane.showMessageDialog(janela, "O depósito de equipamentos está vazio!");
            } else {
                String[] opcoesHerois = janela.getGrupo().obterMenuDeHerois();

                String heroiEscolhido = (String) JOptionPane.showInputDialog(
                        this,
                        "Escolha um Heroi para equipar: ",
                        "Depósito de Equipamentos",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        opcoesHerois,
                        opcoesHerois[0]
                );

                if(heroiEscolhido != null){
                    int indiceHeroi = Integer.parseInt(heroiEscolhido.split(" ")[0]);
                    Heroi heroiSelecionado = janela.getGrupo().getHerois().get(indiceHeroi - 1);

                    String[] opcoesEquipamentos = deposito.obterMenu();

                    String equipamentoEscolhido = (String) JOptionPane.showInputDialog(
                            this,
                            "Escolha um equipamento para " + heroiSelecionado.getNome() + ": ",
                            "Depósito de Equipamentos",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            opcoesEquipamentos,
                            opcoesEquipamentos[0]
                    );

                    if(equipamentoEscolhido != null){
                        int indiceEquipamento = Integer.parseInt(equipamentoEscolhido.split(" ")[0]);
                        Equipamento equipamentoRetirado = deposito.retirar(indiceEquipamento);

                        ResultadoAcao resultado = heroiSelecionado.equipar(equipamentoRetirado, deposito);

                        JOptionPane.showMessageDialog(janela, resultado.getMensagem());
                    }
                }
            }
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {
        // Nada: soltar tecla de movimento é tratado pelo Teclado.
    }
}
