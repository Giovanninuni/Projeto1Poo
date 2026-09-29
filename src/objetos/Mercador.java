package objetos;

import entidades.SpriteMapa;
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

    // Anão barbudo do pacote Momonga (animado no mapa).
    private static final SpriteMapa SPRITE_MAPA = SpriteMapa.personagem(3);

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

    public SpriteMapa getSpriteMapa() {
        return SPRITE_MAPA;
    }
}
		