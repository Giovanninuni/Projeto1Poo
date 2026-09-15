package mundo;

import java.awt.Color;

/**
 * Cada tipo de tile do mapa: diz se bloqueia passagem, qual sprite tentar
 * carregar (em assets/sprites/tiles/) e qual cor usar como placeholder
 * enquanto esse sprite ainda não existir.
 *
 * Os códigos (0, 1, ...) seguem a mesma ideia de um export CSV do Tiled:
 * cada número da matriz do mapa vira um desses tipos.
 */
public enum TipoTile {
    CHAO(0, false, "tiles/chao.png", new Color(90, 90, 90)),
    PAREDE(1, true, "tiles/parede.png", new Color(20, 20, 20));

    private final int codigo;
    private final boolean bloqueiaPassagem;
    private final String arquivoSprite;
    private final Color corPlaceholder;

    TipoTile(int codigo, boolean bloqueiaPassagem, String arquivoSprite, Color corPlaceholder) {
        this.codigo = codigo;
        this.bloqueiaPassagem = bloqueiaPassagem;
        this.arquivoSprite = arquivoSprite;
        this.corPlaceholder = corPlaceholder;
    }

    public static TipoTile porCodigo(int codigo) {
        for (TipoTile tipo : values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código de tile desconhecido: " + codigo);
    }

    public boolean isBloqueiaPassagem() { return bloqueiaPassagem; }
    public String getArquivoSprite() { return arquivoSprite; }
    public Color getCorPlaceholder() { return corPlaceholder; }
}
