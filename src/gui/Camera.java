package gui;

import java.awt.Graphics2D;

/**
 * Decide qual pedaço do mapa aparece na tela e com quanto zoom.
 *
 * Quem desenha continua trabalhando em "pixels do mapa" (tile = 32 px,
 * posição = x * 32). A câmera só transforma o Graphics2D antes: aumenta
 * tudo (scale) e empurra o desenho pro lado (translate), pra que o ponto
 * seguido fique no meio da tela. Assim nenhum método de desenho precisa
 * saber que a câmera existe.
 */
public class Camera {

    private final int zoom;

    // Canto superior esquerdo da parte visível, em pixels do mapa (sem zoom).
    // É double porque, com zoom 2, a câmera pode parar em meio pixel do mapa
    // (= 1 pixel da tela): o herói anda livre, e a câmera acompanha liso.
    private double x;
    private double y;

    /**
     * zoom deve ser inteiro (2, 3...): com zoom quebrado (1.5) cada pixel
     * do desenho viraria 1 ou 2 pixels da tela, e o pixel art ficaria torto.
     */
    public Camera(int zoom) {
        this.zoom = zoom;
    }

    /**
     * Centraliza a câmera no ponto (alvoX, alvoY), sem mostrar nada além da
     * borda do mapa: perto da parede a câmera para e o herói anda até a
     * beirada da tela. Todas as medidas de mapa são em pixels do mapa; as
     * de tela, em pixels reais da janela.
     */
    public void seguir(double alvoX, double alvoY, int larguraMapa, int alturaMapa, int larguraTela, int alturaTela) {
        double larguraVisivel = (double) larguraTela / zoom;
        double alturaVisivel = (double) alturaTela / zoom;

        this.x = alinharAoPixelDaTela(limitar(alvoX - larguraVisivel / 2, larguraMapa, larguraVisivel));
        this.y = alinharAoPixelDaTela(limitar(alvoY - alturaVisivel / 2, alturaMapa, alturaVisivel));
    }

    private static double limitar(double posicao, int tamanhoMapa, double tamanhoVisivel) {
        if (tamanhoMapa <= tamanhoVisivel) {
            // Mapa menor que a tela nessa direção: centraliza e não mexe mais.
            return -(tamanhoVisivel - tamanhoMapa) / 2;
        }
        return Math.max(0, Math.min(posicao, tamanhoMapa - tamanhoVisivel));
    }

    /**
     * Arredonda uma posição do mapa pro pixel de TELA mais próximo (com zoom
     * 2, de meio em meio pixel do mapa). Mais fino que isso não aparece na
     * tela; e sempre caindo num pixel inteiro da tela, o desenho fica nítido.
     */
    public double alinharAoPixelDaTela(double valor) {
        return Math.round(valor * zoom) / (double) zoom;
    }

    /** Aplica zoom e deslocamento: tudo desenhado depois disso já sai no lugar certo. */
    public void aplicar(Graphics2D g) {
        g.scale(zoom, zoom);
        g.translate(-x, -y);
    }

    public int getZoom() {
        return zoom;
    }
}
