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
    private final int[][] idsVisuais;
    private final int largura;
    private final int altura;

    public Mapa(int[][] codigos) {
        this(codigos, semIdsVisuais(codigos.length, codigos[0].length));
    }

    private static int[][] semIdsVisuais(int altura, int largura) {
        int[][] idsVisuais = new int[altura][largura];
        for (int[] linha : idsVisuais) {
            java.util.Arrays.fill(linha, -1);
        }
        return idsVisuais;
    }

    /**
     * idsVisuais guarda, por célula, o índice do tile dentro de uma folha
     * de sprites externa (ex: um tileset feito no Tiled) — usado só pra
     * desenhar o pedaço certo da imagem. -1 quer dizer "sem sprite
     * específico pra essa célula", e quem desenha cai de volta pro
     * placeholder de cor do TipoTile.
     */
    public Mapa(int[][] codigos, int[][] idsVisuais) {
        this.altura = codigos.length;
        this.largura = codigos[0].length;
        this.tiles = new TipoTile[altura][largura];
        this.idsVisuais = idsVisuais;

        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                this.tiles[y][x] = TipoTile.porCodigo(codigos[y][x]);
            }
        }
    }

    public TipoTile getTile(int x, int y) {
        return tiles[y][x];
    }

    public int getIdVisual(int x, int y) {
        return idsVisuais[y][x];
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
}
