package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Carrega imagens de assets/sprites e guarda em cache, pra não ler o
 * arquivo do disco de novo a cada repaint. Se o arquivo ainda não
 * existir (comum enquanto os sprites de tile não foram feitos/baixados),
 * retorna null em vez de travar o jogo — quem chamou decide o que
 * desenhar no lugar (ex: um retângulo placeholder).
 */
public class CarregadorSprites {

    private static final Map<String, BufferedImage> CACHE = new HashMap<>();
    private static final String PASTA_BASE = "assets/sprites/";

    private CarregadorSprites() {}

    public static BufferedImage carregar(String caminhoRelativo) {
        if (CACHE.containsKey(caminhoRelativo)) {
            return CACHE.get(caminhoRelativo);
        }

        BufferedImage imagem;
        try {
            imagem = ImageIO.read(new File(PASTA_BASE + caminhoRelativo));
        } catch (IOException e) {
            imagem = null;
        }

        CACHE.put(caminhoRelativo, imagem);
        return imagem;
    }

    /**
     * Recorta um tile específico de dentro de uma folha de sprites (ex: um
     * tileset exportado do Tiled com vários tiles lado a lado). indice conta
     * da esquerda pra direita, de cima pra baixo, começando em 0 — do jeito
     * que o Tiled também numera. Retorna null se a folha ainda não existir.
     */
    public static BufferedImage recortarTile(String caminhoRelativoFolha, int indice, int colunas, int tamanhoTile) {
        BufferedImage folha = carregar(caminhoRelativoFolha);
        if (folha == null) {
            return null;
        }

        int coluna = indice % colunas;
        int linha = indice / colunas;
        return folha.getSubimage(coluna * tamanhoTile, linha * tamanhoTile, tamanhoTile, tamanhoTile);
    }
}
