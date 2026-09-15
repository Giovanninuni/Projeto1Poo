package mundo;

import java.util.ArrayList;
import java.util.List;

import entidades.Esqueleto;
import entidades.Goblin;
import entidades.Monstro;

/**
 * Modelo do mapa da masmorra: guarda a posição do herói, os limites do
 * grid e os encontros (grupos de monstros) espalhados pelo mapa.
 *
 * Nenhuma linha aqui sabe desenhar nada na tela — isso é responsabilidade
 * da GUI (PainelMasmorra). Esta classe só sabe as regras: para onde o
 * herói pode se mover e quando ele pisa em cima de um encontro.
 */
public class Masmorra {

    /**
     * Um encontro fixo no mapa: uma posição (x, y) e o grupo de monstros
     * que aparece quando o herói pisa ali.
     */
    public static class Encontro {
        private final int x;
        private final int y;
        private final List<Monstro> inimigos;
        private boolean derrotado;

        public Encontro(int x, int y, List<Monstro> inimigos) {
            this.x = x;
            this.y = y;
            this.inimigos = inimigos;
            this.derrotado = false;
        }

        public int getX() { return x; }
        public int getY() { return y; }
        public List<Monstro> getInimigos() { return inimigos; }
        public boolean isDerrotado() { return derrotado; }
        public void marcarDerrotado() { this.derrotado = true; }
    }

    private final Mapa mapa;
    private int heroiX;
    private int heroiY;
    private final List<Encontro> encontros;

    public Masmorra() {
        this(Mapa.criarMapaTeste());
    }

    public Masmorra(Mapa mapa) {
        this.mapa = mapa;
        this.heroiX = 5;
        this.heroiY = 5;
        this.encontros = new ArrayList<>();

        // Encontro de teste que já existia no protótipo original,
        // só que agora vive aqui, e não dentro da classe de GUI.
        List<Monstro> grupoTeste = new ArrayList<>();
        grupoTeste.add(new Goblin("Vitor Santos"));
        grupoTeste.add(new Esqueleto("Vitor Santos 2"));
        this.encontros.add(new Encontro(10, 5, grupoTeste));
    }

    /**
     * Tenta mover o herói por (dx, dy). Retorna false e não move nada se o
     * destino for fora do mapa ou for um tile que bloqueia passagem
     * (ex: parede) — quem decide isso é o Mapa, não a Masmorra.
     */
    public boolean mover(int dx, int dy) {
        int novoX = heroiX + dx;
        int novoY = heroiY + dy;

        if (!mapa.podeAndar(novoX, novoY)) {
            return false;
        }

        this.heroiX = novoX;
        this.heroiY = novoY;
        return true;
    }

    /**
     * Retorna o encontro (ainda não derrotado) na posição atual do herói,
     * ou null se não houver nenhum ali.
     */
    public Encontro getEncontroNaPosicaoDoHeroi() {
        for (Encontro encontro : encontros) {
            if (!encontro.isDerrotado() && encontro.getX() == heroiX && encontro.getY() == heroiY) {
                return encontro;
            }
        }
        return null;
    }

    public int getHeroiX() { return heroiX; }
    public int getHeroiY() { return heroiY; }
    public Mapa getMapa() { return mapa; }
    public List<Encontro> getEncontros() { return encontros; }
}
