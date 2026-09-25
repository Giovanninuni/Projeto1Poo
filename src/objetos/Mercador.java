package objetos;

import loja.Loja;

/**
 * Vendedor parado num tile do mapa. Ele TEM uma loja (composição), mas não
 * É uma: as regras de comércio (ofertas, preços, compra) ficam todas em
 * loja.Loja. O Mercador só sabe onde está e qual loja abre ao interagir.
 */
public class Mercador {
    private final int x;
    private final int y;
    private final Loja loja;

    public Mercador(int x, int y, Loja loja) {
        this.x = x;
        this.y = y;
        this.loja = loja;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Loja getLoja() {
        return loja;
    }
}
		