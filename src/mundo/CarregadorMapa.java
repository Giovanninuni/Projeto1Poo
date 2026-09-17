package mundo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Lê um mapa exportado do Tiled (arquivo .tmx, que é XML) e monta um Mapa.
 *
 * O .tmx guarda, pra cada célula, um "GID" (global tile id) — um número
 * que só faz sentido combinado com o firstgid de cada tileset usado no
 * mapa (cada tileset ocupa uma faixa de números). Aqui a gente só sabe
 * traduzir a faixa do tileset Modicus (as paredes/chão de verdade); tudo
 * que cai fora dessa faixa (célula vazia, ou tile de outro tileset ainda
 * não ligado ao jogo) vira TipoTile.PAREDE e sem sprite específico —
 * quem desenha cai de volta pro placeholder de cor.
 */
public class CarregadorMapa {

    private static final String PASTA_BASE = "assets/mapas/";

    private static final int MODICUS_FIRSTGID = 765;
    private static final int MODICUS_QTD_TILES = 36;
    private static final int MODICUS_ID_CHAO = 5;

    private CarregadorMapa() {}

    public static Mapa carregarDeTmx(String nomeArquivo) {
        try {
            Document documento = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new File(PASTA_BASE + nomeArquivo));

            Element mapaXml = documento.getDocumentElement();
            int largura = Integer.parseInt(mapaXml.getAttribute("width"));
            int altura = Integer.parseInt(mapaXml.getAttribute("height"));

            String textoCsv = documento.getElementsByTagName("data").item(0).getTextContent();
            List<Integer> gids = lerGids(textoCsv);

            int[][] codigos = new int[altura][largura];
            int[][] idsVisuais = new int[altura][largura];

            for (int i = 0; i < gids.size(); i++) {
                int gid = gids.get(i);
                int y = i / largura;
                int x = i % largura;

                int idLocalModicus = gid - MODICUS_FIRSTGID;
                boolean ehModicus = idLocalModicus >= 0 && idLocalModicus < MODICUS_QTD_TILES;

                idsVisuais[y][x] = ehModicus ? idLocalModicus : -1;

                boolean chao = ehModicus && idLocalModicus == MODICUS_ID_CHAO;
                codigos[y][x] = chao ? TipoTile.CHAO.getCodigo() : TipoTile.PAREDE.getCodigo();
            }

            return new Mapa(codigos, idsVisuais);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível carregar o mapa: " + nomeArquivo, e);
        }
    }

    private static List<Integer> lerGids(String textoCsv) {
        List<Integer> gids = new ArrayList<>();
        for (String token : textoCsv.split(",")) {
            String limpo = token.trim();
            if (!limpo.isEmpty()) {
                gids.add(Integer.parseInt(limpo));
            }
        }
        return gids;
    }
}
