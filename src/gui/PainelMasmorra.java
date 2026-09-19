package gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import acoes.ResultadoAcao;
import javax.swing.JOptionPane;
import entidades.Heroi;
import equipamentos.DepositoEquipamentos;
import equipamentos.Equipamento;
import mundo.Mapa;
import mundo.Masmorra;
import mundo.TipoTile;
import objetos.Bau;

public class PainelMasmorra extends JPanel implements KeyListener {

    private JanelaPrincipal janela;
    private Masmorra masmorra;

    private final int TAMANHO_TILE = 32;

    private static final String FOLHA_MODICUS = "tiles/modicus.png";
    private static final int MODICUS_COLUNAS = 4;

    public PainelMasmorra(JanelaPrincipal janela, Masmorra masmorra) {
        this.janela = janela;
        this.masmorra = masmorra;
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        desenharMapa(g);

        // Pinta o herói (Azul) na posição informada pelo modelo
        desenharTile(g, masmorra.getHeroiX(), masmorra.getHeroiY(), Color.BLUE);

        // Pinta cada encontro (Vermelho) que ainda não foi derrotado
        for (Masmorra.Encontro encontro : masmorra.getEncontros()) {
            if (!encontro.isDerrotado()) {
                desenharTile(g, encontro.getX(), encontro.getY(), Color.RED);
            }
        }

        //pinta os baus fechados e abertos de cores diferentes -- Lalae
        for (Bau i: masmorra.getBaus()) {
            Color cor = i.isFechado() ? Color.YELLOW : Color.ORANGE;
            desenharTile(g, i.getX(), i.getY(), cor);
        }

    }

    /**
     * Pinta um tile de cor sólida na posição (x, y) do grid — usado pra
     * herói, encontros e baús, que ainda não têm sprite próprio.
     */
    private void desenharTile(Graphics g, int x, int y, Color cor) {
        g.setColor(cor);
        g.fillRect(x * TAMANHO_TILE, y * TAMANHO_TILE, TAMANHO_TILE, TAMANHO_TILE);
    }

    /**
     * Desenha cada tile do mapa. Se o sprite ainda não existir em
     * assets/sprites/tiles/, cai pro retângulo da cor placeholder do
     * TipoTile — assim o jogo roda normalmente antes dos sprites prontos.
     */
    private void desenharMapa(Graphics g) {
        Mapa mapa = masmorra.getMapa();

        for (int y = 0; y < mapa.getAltura(); y++) {
            for (int x = 0; x < mapa.getLargura(); x++) {
                int px = x * TAMANHO_TILE;
                int py = y * TAMANHO_TILE;
                int idVisual = mapa.getIdVisual(x, y);

                // Se a célula tem um tile específico de uma folha de sprites
                // (ex: veio de um mapa do Tiled), desenha esse recorte. Senão,
                // cai pro sprite único por TipoTile (ou a cor placeholder).
                BufferedImage sprite = (idVisual >= 0)
                        ? CarregadorSprites.recortarTile(FOLHA_MODICUS, idVisual, MODICUS_COLUNAS, TAMANHO_TILE)
                        : CarregadorSprites.carregar(mapa.getTile(x, y).getArquivoSprite());

                if (sprite != null) {
                    g.drawImage(sprite, px, py, TAMANHO_TILE, TAMANHO_TILE, null);
                } else {
                    g.setColor(mapa.getTile(x, y).getCorPlaceholder());
                    g.fillRect(px, py, TAMANHO_TILE, TAMANHO_TILE);
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

        if (dx != 0 || dy != 0) {
            masmorra.mover(dx, dy);
        }

        // Sistema de Colisão: pergunta ao modelo se há um encontro aqui.
        // O encontro só é marcado como derrotado quando o Combate confirma
        // a vitória (JanelaPrincipal.concluirVitoria) — não aqui, no
        // instante de pisar no tile.
        Masmorra.Encontro encontro = masmorra.getEncontroNaPosicaoDoHeroi();
        if (encontro != null) {
            janela.iniciarCombate(encontro);
        }
        
        //abre a tela de status apertando P
        if (tecla == KeyEvent.VK_P){
            janela.mostrarStatus();
            return;
        }

        //checagem para abrir baus apertando E -- Lalae
        if (tecla == KeyEvent.VK_E){
            Bau bauAdjacente = masmorra.getBauAdjacenteAoHeroi();
            
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
