package gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

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

    /**
     * Carrega uma imagem grande (ex: sprite de batalha de 1254x1254) já
     * encolhida pra caber num quadrado tamanho x tamanho — usado pra mostrar
     * herói e monstros no mapa, onde cada um ocupa um tile só. Antes de
     * encolher, corta a borda transparente em volta do desenho (senão o
     * personagem ficaria minúsculo no meio do tile). Mantém a proporção e
     * alinha pela base, pra ele parecer "em pé" no chão. O resultado fica
     * no cache, então o trabalho pesado só acontece uma vez.
     */
    public static BufferedImage carregarMiniatura(String caminhoRelativo, int tamanho) {
        String chave = caminhoRelativo + "@" + tamanho;
        if (CACHE.containsKey(chave)) {
            return CACHE.get(chave);
        }

        BufferedImage original = carregar(caminhoRelativo);
        BufferedImage miniatura = (original == null) ? null : encolher(cortarBordaTransparente(original), tamanho);

        CACHE.put(chave, miniatura);
        return miniatura;
    }

    private static BufferedImage cortarBordaTransparente(BufferedImage imagem) {
        int minX = imagem.getWidth(), minY = imagem.getHeight(), maxX = -1, maxY = -1;
        for (int y = 0; y < imagem.getHeight(); y++) {
            for (int x = 0; x < imagem.getWidth(); x++) {
                boolean visivel = (imagem.getRGB(x, y) >>> 24) != 0; // canal alfa
                if (visivel) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }
        if (maxX < 0) {
            return imagem; // imagem toda transparente: não tem o que cortar
        }
        return imagem.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    private static BufferedImage encolher(BufferedImage imagem, int tamanho) {
        double escala = Math.min((double) tamanho / imagem.getWidth(), (double) tamanho / imagem.getHeight());
        int largura = Math.max(1, (int) Math.round(imagem.getWidth() * escala));
        int altura = Math.max(1, (int) Math.round(imagem.getHeight() * escala));

        // SCALE_SMOOTH faz a média dos pixels (fica bem melhor que só pular
        // pixels ao reduzir tanto). O ImageIcon espera a imagem ficar pronta.
        Image reduzida = new ImageIcon(imagem.getScaledInstance(largura, altura, Image.SCALE_SMOOTH)).getImage();

        BufferedImage resultado = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resultado.createGraphics();
        g.drawImage(reduzida, (tamanho - largura) / 2, tamanho - altura, null);
        g.dispose();
        return resultado;
    }
}
