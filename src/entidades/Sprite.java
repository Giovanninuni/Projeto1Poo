package entidades;

/**
 * Onde fica o desenho de um personagem: em qual folha de sprites e em que
 * linha/coluna da grade (cada célula tem 32x32 px, contando do 0).
 *
 * Só guarda a "localização" do desenho; quem abre a imagem e recorta é a
 * GUI (CarregadorSprites). É um record: uma classe só de dados, em que o
 * Java já gera sozinho construtor, getters (folha(), linha(), coluna()),
 * equals e toString.
 *
 * As folhas são do pacote 32rogues (assets/sprites/32rogues/). Pra achar a
 * posição de um sprite novo, abra rogues.png ou monsters.png e conte as
 * células — os números dos .txt que vêm junto nem sempre batem com a grade.
 */
public record Sprite(String folha, int linha, int coluna) {

    public static final int TAMANHO = 32;

    private static final String FOLHA_ROGUES = "32rogues/rogues.png";
    private static final String FOLHA_MONSTROS = "32rogues/monsters.png";

    /** Heróis e pessoas (rogues.png, grade 7x7). */
    public static Sprite rogue(int linha, int coluna) {
        return new Sprite(FOLHA_ROGUES, linha, coluna);
    }

    /** Monstros (monsters.png, grade 12x13). */
    public static Sprite monstro(int linha, int coluna) {
        return new Sprite(FOLHA_MONSTROS, linha, coluna);
    }
}
