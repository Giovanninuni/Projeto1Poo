package gui;

import javax.swing.JPanel;

import contratos.ResultadoAcao;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import entidades.Heroi;
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

    // O tileset da masmorra não tem personagens: herói e monstros usam os
    // sprites de batalha, encolhidos pra caber num tile (ver CarregadorSprites).
    private static final String SPRITE_HEROI = "heroi_batalha_recortado.png";

    // Zoom 2x: cada tile de 32 px aparece com 64 px na tela (16x12 tiles
    // visíveis numa janela 1024x768), e a câmera acompanha o herói.
    private final Camera camera = new Camera(2);

    public PainelMasmorra(JanelaPrincipal janela, Masmorra masmorra) {
        this.janela = janela;
        this.masmorra = masmorra;
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // A câmera mira o centro do tile do herói.
        Mapa mapa = masmorra.getMapa();
        camera.seguir(
                masmorra.getHeroiX() * TAMANHO_TILE + TAMANHO_TILE / 2,
                masmorra.getHeroiY() * TAMANHO_TILE + TAMANHO_TILE / 2,
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
        desenharCena(g2);
        g2.dispose();
    }

    /**
     * Desenha tudo em pixels do mapa (tile = 32), como se não houvesse
     * câmera — o zoom e o deslocamento já foram aplicados no Graphics.
     */
    private void desenharCena(Graphics g) {
        // Ordem importa: o que é desenhado depois fica por cima.
        desenharMapa(g);

        //baus fechados e abertos com sprites diferentes -- Lalae
        for (Bau i: masmorra.getBaus()) {
            int id = i.isFechado() ? TilesetMasmorra.BAU_FECHADO : TilesetMasmorra.BAU_VAZIO;
            desenharImagem(g, tileDoTileset(id), i.getX(), i.getY(), Color.YELLOW);
        }

        // Cada encontro que ainda não foi derrotado aparece com o sprite do
        // primeiro monstro do grupo (ex: 3 goblins -> um goblin no mapa).
        for (Masmorra.Encontro encontro : masmorra.getEncontros()) {
            if (!encontro.isDerrotado()) {
                String arquivo = encontro.getInimigos().get(0).getArquivoSprite();
                BufferedImage sprite = CarregadorSprites.carregarMiniatura(arquivo, tamanhoMiniatura());
                desenharImagem(g, sprite, encontro.getX(), encontro.getY(), Color.RED);
            }
        }

        // Mercador (Verde) ainda sem sprite
        for (Mercador mercador : masmorra.getMercadores()) {
            desenharTile(g, mercador.getX(), mercador.getY(), Color.GREEN);
        }

        // Herói por último, pra ficar por cima de tudo (ex: parado em cima
        // de um encontro do qual acabou de fugir).
        BufferedImage spriteHeroi = CarregadorSprites.carregarMiniatura(SPRITE_HEROI, tamanhoMiniatura());
        desenharImagem(g, spriteHeroi, masmorra.getHeroiX(), masmorra.getHeroiY(), Color.BLUE);
    }

    /**
     * As miniaturas são geradas já no tamanho da tela (32 x zoom = 64 px):
     * desenhadas num tile de 32 com zoom 2x, cada pixel delas cai em um
     * pixel da tela, e o personagem sai com o dobro de detalhe.
     */
    private int tamanhoMiniatura() {
        return TAMANHO_TILE * camera.getZoom();
    }

    /**
     * Pinta um tile de cor sólida na posição (x, y) do grid — usado pra
     * quem ainda não tem sprite próprio (mercador) ou quando a imagem
     * não foi encontrada.
     */
    private void desenharTile(Graphics g, int x, int y, Color cor) {
        g.setColor(cor);
        g.fillRect(x * TAMANHO_TILE, y * TAMANHO_TILE, TAMANHO_TILE, TAMANHO_TILE);
    }

    /**
     * Desenha uma imagem na posição (x, y) do grid. Se a imagem não existir
     * (arquivo faltando), cai pro retângulo da corReserva; se corReserva
     * for null, simplesmente não desenha nada.
     */
    private void desenharImagem(Graphics g, BufferedImage imagem, int x, int y, Color corReserva) {
        if (imagem != null) {
            g.drawImage(imagem, x * TAMANHO_TILE, y * TAMANHO_TILE, TAMANHO_TILE, TAMANHO_TILE, null);
        } else if (corReserva != null) {
            desenharTile(g, x, y, corReserva);
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
        int tecla = e.getKeyCode();

        int dx = 0;
        int dy = 0;
        if (tecla == KeyEvent.VK_W || tecla == KeyEvent.VK_UP) dy = -1;
        if (tecla == KeyEvent.VK_S || tecla == KeyEvent.VK_DOWN) dy = 1;
        if (tecla == KeyEvent.VK_A || tecla == KeyEvent.VK_LEFT) dx = -1;
        if (tecla == KeyEvent.VK_D || tecla == KeyEvent.VK_RIGHT) dx = 1;
        
        /* FOI FEITA UMA MUDANÇA AQUI ABAIXO, PARA CORRIGIR UM BUG EM QUE APÓS FUGIR DA BATALHA, ANDAR 
        FAZIA A BATALHA VOLTAR */

        if ((dx != 0 || dy != 0) && masmorra.mover(dx, dy)) {
            Masmorra.Encontro encontro = masmorra.getEncontroNaPosicaoDoHeroi();
            if (encontro != null) {
                janela.iniciarCombate(encontro);
                return;
            }
        }
        
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
    public void keyReleased(KeyEvent e) {}
}
