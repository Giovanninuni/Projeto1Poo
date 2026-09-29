package entidades;

/**
 * Aparência animada de um personagem NO MAPA (pacote Momonga, em
 * assets/sprites/momonga.png). Na batalha continua valendo o Sprite
 * (32rogues); no mapa o desenho é menor, estilo chibi, e animado.
 *
 * Na folha, cada personagem ocupa 2 linhas de 4 quadros de 32x32: a de
 * cima é "parado" (respirando) e a de baixo é "andando". Todos olham pra
 * direita; pra andar pra esquerda, a GUI espelha a imagem.
 *
 * x e y são, em pixels, o canto do primeiro quadro "parado". Guardamos
 * pixels (e não linha/coluna como no Sprite) porque o bloco dos monstros
 * começa no meio de uma célula da folha (x = 272, que não é múltiplo de 32).
 */
public record SpriteMapa(int x, int y) {

    public static final String FOLHA = "momonga.png";
    public static final int TAMANHO = 32;
    public static final int QUADROS = 4;

    // A folha tem dois blocos lado a lado: personagens à esquerda e
    // monstros à direita. Cada personagem ocupa 2 linhas (parado + andando).
    private static final int X_PERSONAGENS = 0;
    private static final int X_MONSTROS = 272;
    private static final int ALTURA_PERSONAGEM = 2 * TAMANHO;

    /**
     * Bloco dos personagens: 0 = cabelo vermelho, 1 = cavaleiro,
     * 2 = encapuzado azul, 3 = anão barbudo.
     */
    public static SpriteMapa personagem(int indice) {
        return new SpriteMapa(X_PERSONAGENS, indice * ALTURA_PERSONAGEM);
    }

    /**
     * Bloco dos monstros: 0 = esqueleto de braço estendido, 1 = cavaleiro
     * esqueleto, 2 = esqueleto encapuzado, 3 = esqueleto, 4 = goblin.
     */
    public static SpriteMapa monstro(int indice) {
        return new SpriteMapa(X_MONSTROS, indice * ALTURA_PERSONAGEM);
    }
}
