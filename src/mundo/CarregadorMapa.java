package mundo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Lê um mapa escrito como texto (assets/mapas/*.txt) e monta um Mapa.
 * Cada caractere do arquivo é uma célula do grid:
 *
 *   #  parede (topo)             (espaço)  vazio / fora do mapa
 *   w  face da parede (tijolos)  T  face com tocha
 *   k  face rachada              g  face com musgo
 *   D  porta fechada             d  porta aberta (dá pra passar)
 *   .  chão     c  chão rachado     m  chão com musgo     r  cascalho
 *   S  escada   B  ossos (enfeite no chão, dá pra passar por cima)
 *
 * Regras de montagem (visão 3/4, igual Pokémon/Final Fantasy):
 *  - a parede de cima de uma sala tem 2 linhas de face logo abaixo do '#'
 *    (a de cima vira FACE_ALTA e a de baixo FACE_BAIXA automaticamente);
 *  - portas também ocupam essas 2 linhas (coloque 'D' ou 'd' nas duas);
 *  - bordas, cantos e pontas das paredes são escolhidos sozinhos
 *    (autotile): o carregador olha os vizinhos de cada célula e escolhe
 *    o tile que encaixa.
 *
 * Baús, encontros e mercadores NÃO ficam no texto: eles têm estado
 * (aberto/fechado, derrotado...) e são criados na Masmorra.
 */
public class CarregadorMapa {

    private static final String PASTA_BASE = "assets/mapas/";

    private static final String FACES = "wTkgDd";
    private static final String CHAOS = ".cmrSB";

    private final char[][] grade;
    private final int largura;
    private final int altura;

    private CarregadorMapa(List<String> linhas) {
        this.altura = linhas.size();
        int maiorLinha = 0;
        for (String linha : linhas) {
            maiorLinha = Math.max(maiorLinha, linha.length());
        }
        this.largura = maiorLinha;

        // Linhas mais curtas são completadas com espaço (vazio).
        this.grade = new char[altura][largura];
        for (int y = 0; y < altura; y++) {
            String linha = linhas.get(y);
            for (int x = 0; x < largura; x++) {
                grade[y][x] = x < linha.length() ? linha.charAt(x) : ' ';
            }
        }
    }

    public static Mapa carregarDeTexto(String nomeArquivo) {
        List<String> linhas;
        try {
            linhas = new ArrayList<>(Files.readAllLines(Path.of(PASTA_BASE + nomeArquivo), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar o mapa: " + nomeArquivo, e);
        }

        // Ignora linhas em branco no fim do arquivo (enter a mais no final).
        while (!linhas.isEmpty() && linhas.get(linhas.size() - 1).isBlank()) {
            linhas.remove(linhas.size() - 1);
        }
        return new CarregadorMapa(linhas).montar();
    }

    private Mapa montar() {
        int[][] codigos = new int[altura][largura];
        int[][] idsVisuais = new int[altura][largura];
        int[][] idsDecoracao = new int[altura][largura];

        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                char c = grade[y][x];
                idsVisuais[y][x] = tileVisual(x, y, c);
                idsDecoracao[y][x] = (c == 'B') ? TilesetMasmorra.OSSOS : -1;
                codigos[y][x] = ehAndavel(c) ? TipoTile.CHAO.getCodigo() : TipoTile.PAREDE.getCodigo();
            }
        }
        return new Mapa(codigos, idsVisuais, idsDecoracao);
    }

    // ---------------------------------------------------------------- consultas

    private char ch(int x, int y) {
        if (x < 0 || y < 0 || x >= largura || y >= altura) {
            return '#'; // fora do mapa conta como parede
        }
        return grade[y][x];
    }

    private static boolean ehFace(char c) {
        return FACES.indexOf(c) >= 0;
    }

    private static boolean ehAndavel(char c) {
        return CHAOS.indexOf(c) >= 0 || c == 'd';
    }

    /** topo de parede (ou vazio) */
    private boolean ehParede(int x, int y) {
        char c = ch(x, y);
        return c == '#' || c == ' ';
    }

    /** tudo que não é chão */
    private boolean ehSolido(int x, int y) {
        return ehParede(x, y) || ehFace(ch(x, y));
    }

    // ---------------------------------------------------------------- autotile

    private int tileVisual(int x, int y, char c) {
        if (c == ' ') {
            return TilesetMasmorra.VAZIO;
        }
        if (c == '#') {
            return tileParede(x, y);
        }
        if (ehFace(c)) {
            return tileFace(x, y, c);
        }
        if (CHAOS.indexOf(c) >= 0) {
            return tileChao(x, y, c);
        }
        throw new IllegalArgumentException(
                "Caractere '" + c + "' desconhecido no mapa (linha " + (y + 1) + ", coluna " + (x + 1) + ")");
    }

    private int tileParede(int x, int y) {
        int m = 0; // bit ligado = lado aberto
        if (!ehParede(x, y - 1)) m |= 1; // N
        if (!ehParede(x + 1, y)) m |= 2; // L
        if (!ehParede(x, y + 1)) m |= 4; // S
        if (!ehParede(x - 1, y)) m |= 8; // O
        if (m == 0) { // cercado: talvez um canto interno
            if (!ehParede(x + 1, y + 1)) return TilesetMasmorra.PAREDE_INT_SE;
            if (!ehParede(x - 1, y + 1)) return TilesetMasmorra.PAREDE_INT_SO;
            if (!ehParede(x + 1, y - 1)) return TilesetMasmorra.PAREDE_INT_NE;
            if (!ehParede(x - 1, y - 1)) return TilesetMasmorra.PAREDE_INT_NO;
        }
        return TilesetMasmorra.PAREDE_POR_MASCARA[m];
    }

    private int tileFace(int x, int y, char c) {
        boolean alta = ehParede(x, y - 1); // logo abaixo do topo da parede
        switch (c) {
            case 'D': return alta ? TilesetMasmorra.PORTA_TOPO : TilesetMasmorra.PORTA_BASE;
            case 'd': return alta ? TilesetMasmorra.PORTA_ABERTA_TOPO : TilesetMasmorra.PORTA_ABERTA_BASE;
            case 'k': return alta ? TilesetMasmorra.FACE_ALTA_RACHADA : TilesetMasmorra.FACE_BAIXA_RACHADA;
            case 'T': if (alta) return TilesetMasmorra.FACE_ALTA_TOCHA; break;
            case 'g': if (!alta) return TilesetMasmorra.FACE_BAIXA_MUSGO; break;
            default: break;
        }
        boolean temEsq = ehFace(ch(x - 1, y));
        boolean temDir = ehFace(ch(x + 1, y));
        if (!temEsq) return alta ? TilesetMasmorra.FACE_ALTA_ESQ : TilesetMasmorra.FACE_BAIXA_ESQ;
        if (!temDir) return alta ? TilesetMasmorra.FACE_ALTA_DIR : TilesetMasmorra.FACE_BAIXA_DIR;
        return alta ? TilesetMasmorra.FACE_ALTA : TilesetMasmorra.FACE_BAIXA;
    }

    private int tileChao(int x, int y, char c) {
        if (c == 'S') {
            return TilesetMasmorra.ESCADA;
        }
        int m = 0; // bit ligado = tem parede
        if (ehSolido(x, y - 1)) m |= 1;
        if (ehSolido(x + 1, y)) m |= 2;
        if (ehSolido(x, y + 1)) m |= 4;
        if (ehSolido(x - 1, y)) m |= 8;
        if (m != 0) {
            return TilesetMasmorra.CHAO_POR_MASCARA[m];
        }
        if (ehSolido(x - 1, y - 1)) return TilesetMasmorra.BORDA_INT_NO;
        if (ehSolido(x + 1, y - 1)) return TilesetMasmorra.BORDA_INT_NE;
        if (ehSolido(x - 1, y + 1)) return TilesetMasmorra.BORDA_INT_SO;
        if (ehSolido(x + 1, y + 1)) return TilesetMasmorra.BORDA_INT_SE;
        switch (c) { // variações só no chão do meio
            case 'c': return TilesetMasmorra.CHAO_RACHADO;
            case 'm': return TilesetMasmorra.CHAO_MUSGO;
            case 'r': return TilesetMasmorra.CHAO_CASCALHO;
            default:  return TilesetMasmorra.CHAO;
        }
    }
}
