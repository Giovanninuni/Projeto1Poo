package gui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import entidades.Sprite;
import entidades.SpriteMapa;

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
     * O recorte fica no cache: sem ele, cada repaint da masmorra criava
     * centenas de imagens novas (uma por tile desenhado).
     */
    public static BufferedImage recortarTile(String caminhoRelativoFolha, int indice, int colunas, int tamanhoTile) {
        String chave = caminhoRelativoFolha + "#" + indice + "/" + tamanhoTile;
        if (CACHE.containsKey(chave)) {
            return CACHE.get(chave);
        }

        BufferedImage folha = carregar(caminhoRelativoFolha);
        BufferedImage recorte = null;
        if (folha != null) {
            int coluna = indice % colunas;
            int linha = indice / colunas;
            recorte = folha.getSubimage(coluna * tamanhoTile, linha * tamanhoTile, tamanhoTile, tamanhoTile);
        }

        CACHE.put(chave, recorte);
        return recorte;
    }

    /**
     * Recorta o desenho de um personagem da folha dele (ver entidades.Sprite)
     * e amplia pela escala pedida (1 = 32 px, 4 = 128 px...). A ampliação é
     * por "vizinho mais próximo": cada pixel vira um quadradinho nítido, que
     * é o jeito certo de aumentar pixel art. Fica no cache, então cada
     * sprite só é recortado/ampliado uma vez. Retorna null se a folha não
     * existir.
     */
    public static BufferedImage recortarSprite(Sprite sprite, int escala) {
        String chave = sprite.folha() + "@" + sprite.linha() + "," + sprite.coluna() + "x" + escala;
        if (CACHE.containsKey(chave)) {
            return CACHE.get(chave);
        }

        BufferedImage folha = carregar(sprite.folha());
        BufferedImage resultado = null;
        if (folha != null) {
            int t = Sprite.TAMANHO;
            BufferedImage recorte = folha.getSubimage(sprite.coluna() * t, sprite.linha() * t, t, t);

            resultado = new BufferedImage(t * escala, t * escala, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = resultado.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g.drawImage(recorte, 0, 0, t * escala, t * escala, null);
            g.dispose();
        }

        CACHE.put(chave, resultado);
        return resultado;
    }

    /**
     * Recorta um quadro da animação de mapa (ver entidades.SpriteMapa).
     * andando escolhe a linha da folha (parado ou andando), quadro vai de
     * 0 a 3, e espelhado vira o desenho pra esquerda (a folha só tem os
     * personagens olhando pra direita). Cada combinação é recortada uma
     * vez só e fica no cache. Retorna null se a folha não existir.
     */
    public static BufferedImage recortarQuadro(SpriteMapa sprite, boolean andando, int quadro, boolean espelhado) {
        String chave = SpriteMapa.FOLHA + "@" + sprite.x() + "," + sprite.y()
                + (andando ? "/andando" : "/parado") + quadro + (espelhado ? "/espelhado" : "");
        if (CACHE.containsKey(chave)) {
            return CACHE.get(chave);
        }

        BufferedImage folha = carregar(SpriteMapa.FOLHA);
        BufferedImage resultado = null;
        if (folha != null) {
            int t = SpriteMapa.TAMANHO;
            int x = sprite.x() + quadro * t;
            int y = sprite.y() + (andando ? t : 0); // a linha "andando" fica logo abaixo da "parado"
            resultado = folha.getSubimage(x, y, t, t);

            if (espelhado) {
                // Desenhar com o x de destino invertido (de t até 0) vira a
                // imagem na horizontal, como um espelho.
                BufferedImage virada = new BufferedImage(t, t, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = virada.createGraphics();
                g.drawImage(resultado, t, 0, 0, t, 0, 0, t, t, null);
                g.dispose();
                resultado = virada;
            }
        }

        CACHE.put(chave, resultado);
        return resultado;
    }
}
