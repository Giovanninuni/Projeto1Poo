package mundo;

/**
 * Números (IDs) de cada tile dentro da folha tileset_masmorra.png — uma
 * imagem de 8 x 8 tiles de 32 px. O ID de um tile é: linha * 8 + coluna
 * (a imagem tileset_masmorra_guia.png, na mesma pasta, mostra cada ID).
 *
 * Esta classe só guarda os números: quem abre a imagem e recorta o
 * pedaço certo é a GUI (PainelMasmorra + CarregadorSprites). Assim o
 * pacote mundo continua sem saber desenhar nada.
 */
public final class TilesetMasmorra {

    public static final String ARQUIVO = "tiles/tileset_masmorra.png";
    public static final int COLUNAS = 8;

    // ---------------------------------------------------------------- chão
    public static final int CHAO = 9;
    public static final int CHAO_RACHADO = 40, CHAO_MUSGO = 41, CHAO_CASCALHO = 42, ESCADA = 43;
    // chão com parede só na diagonal (canto interno)
    public static final int BORDA_INT_NO = 32, BORDA_INT_NE = 33, BORDA_INT_SO = 34, BORDA_INT_SE = 35;

    // ---------------------------------------------------------------- topo da parede
    public static final int VAZIO = 44;
    public static final int PAREDE_INT_NO = 36, PAREDE_INT_NE = 37, PAREDE_INT_SO = 38, PAREDE_INT_SE = 39;

    // ---------------------------------------------------------------- face da parede (2 tiles de altura)
    public static final int FACE_ALTA_ESQ = 48, FACE_ALTA = 49, FACE_ALTA_DIR = 50;
    public static final int FACE_ALTA_TOCHA = 51, FACE_ALTA_RACHADA = 52;
    public static final int FACE_BAIXA_ESQ = 56, FACE_BAIXA = 57, FACE_BAIXA_DIR = 58;
    public static final int FACE_BAIXA_MUSGO = 59, FACE_BAIXA_RACHADA = 60;

    // ---------------------------------------------------------------- portas (topo em cima, base embaixo)
    public static final int PORTA_TOPO = 53, PORTA_BASE = 61;
    public static final int PORTA_ABERTA_TOPO = 54, PORTA_ABERTA_BASE = 62;

    // ---------------------------------------------------------------- objetos (fundo transparente)
    public static final int BAU_FECHADO = 45, BAU_ABERTO = 46, BAU_VAZIO = 47;
    public static final int POTE = 55, OSSOS = 63;

    /**
     * Autotile por máscara de 4 bits: Norte=1, Leste=2, Sul=4, Oeste=8.
     * A máscara é o índice do vetor; o valor é o ID do tile.
     *
     * CHAO_POR_MASCARA:   bit ligado = tem parede daquele lado do chão
     *                     (ex: 1 = parede só ao norte -> BORDA_N).
     * PAREDE_POR_MASCARA: bit ligado = aquele lado da parede é aberto
     *                     (ex: 4 = chão só ao sul -> PAREDE_TOPO_S).
     */
    public static final int[] CHAO_POR_MASCARA   = {9, 1, 10, 2, 17, 25, 18, 26, 8, 0, 11, 3, 16, 24, 19, 27};
    public static final int[] PAREDE_POR_MASCARA = {13, 5, 14, 6, 21, 29, 22, 30, 12, 4, 15, 7, 20, 28, 23, 31};

    private TilesetMasmorra() {}
}
