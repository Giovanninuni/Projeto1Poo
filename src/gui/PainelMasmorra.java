package gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import acoes.ResultadoAcao;
import javax.swing.JOptionPane;
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
        g.setColor(Color.BLUE);
        g.fillRect(masmorra.getHeroiX() * TAMANHO_TILE, masmorra.getHeroiY() * TAMANHO_TILE, TAMANHO_TILE, TAMANHO_TILE);

        // Pinta cada encontro (Vermelho) que ainda não foi derrotado
        g.setColor(Color.RED);
        for (Masmorra.Encontro encontro : masmorra.getEncontros()) {
            if (!encontro.isDerrotado()) {
                g.fillRect(encontro.getX() * TAMANHO_TILE, encontro.getY() * TAMANHO_TILE, TAMANHO_TILE, TAMANHO_TILE);
            }
        }
        
        
        //pinta os baus fechados e abertos de cores diferentes -- Lalae
        for (Bau i: masmorra.getBaus()) {
            if (i.isFechado()) {
                g.setColor(Color.YELLOW);
                g.fillRect(i.getX() * TAMANHO_TILE, i.getY() * TAMANHO_TILE, TAMANHO_TILE, TAMANHO_TILE);
            }else{
                g.setColor(Color.ORANGE);
                g.fillRect(i.getX() * TAMANHO_TILE, i.getY() * TAMANHO_TILE, TAMANHO_TILE, TAMANHO_TILE);
            }
        }
        
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
                    
                    ResultadoAcao abriuBau = bauAdjacente.abrir(janela.getGrupo().getHerois().get(indice - 1));
                
                    JOptionPane.showMessageDialog(janela, abriuBau.getMensagem());
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
