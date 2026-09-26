package mundo;

/**
 * Grid de tiles do mapa: cada posição (x, y) guarda um TipoTile.
 *
 * As matrizes de entrada usam a convenção [linha][coluna] (codigos[y][x]),
 * que é a ordem natural de um mapa lido linha por linha de um arquivo.
 */
public class Mapa {

    private final TipoTile[][] tiles;
    private final int[][] idsVisuais;
    private final int[][] idsDecoracao;
    private final int largura;
    private final int altura;

    public Mapa(int[][] codigos) {
        this(codigos, semIds(codigos.length, codigos[0].length));
    }

    public Mapa(int[][] codigos, int[][] idsVisuais) {
        this(codigos, idsVisuais, semIds(codigos.length, codigos[0].length));
    }

    private static int[][] semIds(int altura, int largura) {
        int[][] ids = new int[altura][largura];
        for (int[] linha : ids) {
            java.util.Arrays.fill(linha, -1);
        }
        return ids;
    }

    /**
     * idsVisuais guarda, por célula, o índice do tile dentro da folha de
     * sprites (ver TilesetMasmorra) — usado só pra desenhar o pedaço certo
     * da imagem. idsDecoracao é uma segunda camada desenhada por cima
     * (ex: ossos no chão, que têm fundo transparente). Nas duas, -1 quer
     * dizer "nada aqui"; sem id visual, quem desenha cai de volta pro
     * placeholder de cor do TipoTile.
     */
    public Mapa(int[][] codigos, int[][] idsVisuais, int[][] idsDecoracao) {
        this.altura = codigos.length;
        this.largura = codigos[0].length;
        this.tiles = new TipoTile[altura][largura];
        this.idsVisuais = idsVisuais;
        this.idsDecoracao = idsDecoracao;

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

    public int getIdDecoracao(int x, int y) {
        return idsDecoracao[y][x];
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
