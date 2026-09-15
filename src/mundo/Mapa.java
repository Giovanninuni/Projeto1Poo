package mundo;

/**
 * Grid de tiles do mapa: cada posição (x, y) guarda um TipoTile.
 *
 * A matriz de entrada usa a convenção [linha][coluna] (codigos[y][x]) —
 * é assim que o Tiled exporta um CSV de camada de tiles, então quando o
 * mapa vier de um arquivo exportado de lá, ele encaixa direto aqui sem
 * mudar mais nada no resto do jogo.
 */
public class Mapa {

    private final TipoTile[][] tiles;
    private final int largura;
    private final int altura;

    public Mapa(int[][] codigos) {
        this.altura = codigos.length;
        this.largura = codigos[0].length;
        this.tiles = new TipoTile[altura][largura];

        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                this.tiles[y][x] = TipoTile.porCodigo(codigos[y][x]);
            }
        }
    }

    public TipoTile getTile(int x, int y) {
        return tiles[y][x];
    }

    /**
     * Diz se dá pra andar em (x, y): precisa estar dentro do mapa e o
     * tile ali não pode bloquear passagem (ex: parede).
     */
    public boolean podeAndar(int x, int y) {
        if (x < 0 || x >= largura || y < 0 || y >= altura) {
            return false;
        }
        return !tiles[y][x].isBloqueiaPassagem();
    }

    public int getLargura() { return largura; }
    public int getAltura() { return altura; }

    /**
     * Mapa de teste pra usar enquanto não temos um mapa feito no Tiled:
     * um retângulo de chão cercado por parede nas bordas.
     */
    public static Mapa criarMapaTeste() {
        int largura = 20;
        int altura = 15;
        int[][] codigos = new int[altura][largura];

        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                boolean borda = (x == 0 || y == 0 || x == largura - 1 || y == altura - 1);
                codigos[y][x] = borda ? 1 : 0;
            }
        }

        return new Mapa(codigos);
    }
}
