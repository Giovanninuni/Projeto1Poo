package mundo;

import java.util.ArrayList;
import java.util.List;

import entidades.Esqueleto;
import entidades.Goblin;
import entidades.Monstro;
import equipamentos.CatalogoDeEquipamentos;
import itens.*;
import loja.CatalogoDeLojas;
import objetos.Bau;
import objetos.Mercador;

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
    

    public static final int TAMANHO_TILE = 32;

    // Caixa de colisão do herói: só a região dos pés, mais estreita que o
    // tile (18 x 12 px). Assim ele encosta nas coisas de um jeito natural
    // numa visão 3/4: a cabeça pode "passar na frente" da parede de cima.
    private static final double MEIA_LARGURA_COLISAO = 9;
    private static final double ALTURA_COLISAO = 12;

    private final Mapa mapa;

    // Posição do herói em pixels do mapa (tile = 32): x é o meio dos pés e
    // y é a linha dos pés (o fundo da caixa de colisão). Com o movimento
    // livre ele pode estar entre dois tiles; getHeroiX/getHeroiY dizem em
    // qual tile ele está.
    private double heroiPx;
    private double heroiPy;
    private final List<Encontro> encontros;
    private final List<Bau> baus;
    private final List<Mercador> mercadores;

    public Masmorra(Mapa mapa) {
        this.mapa = mapa;
        posicionarHeroiNoTile(5, 5);
        this.encontros = new ArrayList<>();
        
        

        // Encontro de teste que já existia no protótipo original,
        // só que agora vive aqui, e não dentro da classe de GUI.
        List<Monstro> grupoTeste = new ArrayList<>();
        grupoTeste.add(new Goblin("Goblin 1"));
        grupoTeste.add(new Goblin("Goblin 2"));
        grupoTeste.add(new Goblin("Goblin 3"));
        this.encontros.add(new Encontro(10, 5, grupoTeste));
        
        this.baus = new ArrayList<>();

        List<Item> itensDoBauUm = new ArrayList<>();
        itensDoBauUm.add(CatalogoDeItens.pocaoDeMana());
        itensDoBauUm.add(CatalogoDeItens.pocaoDeVida());
        this.baus.add(new Bau(17, 6, itensDoBauUm, List.of(CatalogoDeEquipamentos.acessorioDoViajante())));

        List<Item> itensDoBauDois = new ArrayList<>();
        itensDoBauDois.add(CatalogoDeItens.pocaoDeVida());
        this.baus.add(new Bau(10, 10, itensDoBauDois, List.of(CatalogoDeEquipamentos.cajadoAncestral())));

        // Mercador perto do ponto de partida, longe do encontro e dos baús.
        // O que ele vende é definido no CatalogoDeLojas, não aqui.
        this.mercadores = new ArrayList<>();
        this.mercadores.add(new Mercador(5, 12, CatalogoDeLojas.lojaDaMasmorra()));
    }

    // Coloca o herói no meio do tile (x, y), com os pés perto do fundo dele.
    private void posicionarHeroiNoTile(int x, int y) {
        this.heroiPx = x * TAMANHO_TILE + TAMANHO_TILE / 2.0;
        this.heroiPy = (y + 1) * TAMANHO_TILE - 2;
    }

    /**
     * Move o herói (dx, dy) pixels, um eixo de cada vez: primeiro x, depois
     * y. Se o eixo bater em algo (parede, baú, mercador), o herói encosta
     * nele e para só nesse eixo — o outro continua. É isso que faz ele
     * "deslizar" pela parede ao andar na diagonal encostado nela.
     *
     * Retorna true se o herói saiu do lugar.
     */
    public boolean moverHeroi(double dx, double dy) {
        double antesX = heroiPx;
        double antesY = heroiPy;

        if (dx != 0) {
            double novoX = heroiPx + dx;
            heroiPx = caixaLivre(novoX, heroiPy) ? novoX : encostarX(novoX, dx);
        }
        if (dy != 0) {
            double novoY = heroiPy + dy;
            heroiPy = caixaLivre(heroiPx, novoY) ? novoY : encostarY(novoY, dy);
        }

        return heroiPx != antesX || heroiPy != antesY;
    }

    /**
     * A caixa de colisão do herói, com os pés em (px, py), cabe aqui? Olha
     * todos os tiles que a caixa encosta (normalmente 1 a 4) e todos têm
     * que estar livres.
     */
    private boolean caixaLivre(double px, double py) {
        // O "- 0.001" trata a borda direita/de baixo como aberta: encostar
        // exatamente na linha de um tile não conta como estar dentro dele.
        int tileEsquerda = (int) Math.floor((px - MEIA_LARGURA_COLISAO) / TAMANHO_TILE);
        int tileDireita = (int) Math.floor((px + MEIA_LARGURA_COLISAO - 0.001) / TAMANHO_TILE);
        int tileCima = (int) Math.floor((py - ALTURA_COLISAO) / TAMANHO_TILE);
        int tileBaixo = (int) Math.floor((py - 0.001) / TAMANHO_TILE);

        for (int y = tileCima; y <= tileBaixo; y++) {
            for (int x = tileEsquerda; x <= tileDireita; x++) {
                if (!podeOcupar(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Bateu andando na horizontal: cola a lateral da caixa na borda do tile
    // que bloqueou (em vez de parar alguns pixels antes dele).
    private double encostarX(double novoX, double dx) {
        if (dx > 0) {
            int tileBloqueio = (int) Math.floor((novoX + MEIA_LARGURA_COLISAO - 0.001) / TAMANHO_TILE);
            return tileBloqueio * TAMANHO_TILE - MEIA_LARGURA_COLISAO;
        }
        int tileBloqueio = (int) Math.floor((novoX - MEIA_LARGURA_COLISAO) / TAMANHO_TILE);
        return (tileBloqueio + 1) * TAMANHO_TILE + MEIA_LARGURA_COLISAO;
    }

    // Mesma ideia na vertical: cola o topo ou o fundo da caixa no tile.
    private double encostarY(double novoY, double dy) {
        if (dy > 0) {
            int tileBloqueio = (int) Math.floor((novoY - 0.001) / TAMANHO_TILE);
            return tileBloqueio * TAMANHO_TILE;
        }
        int tileBloqueio = (int) Math.floor((novoY - ALTURA_COLISAO) / TAMANHO_TILE);
        return (tileBloqueio + 1) * TAMANHO_TILE + ALTURA_COLISAO;
    }

    // Um tile onde o herói pode ficar: dentro do mapa, sem parede, sem baú
    // e sem mercador (encontros não bloqueiam: pisar neles inicia a batalha).
    private boolean podeOcupar(int x, int y) {
        return mapa.podeAndar(x, y) && !existeBau(x, y) && !existeMercador(x, y);
    }

    /**
     * Retorna o encontro (ainda não derrotado) na posição atual do herói,
     * ou null se não houver nenhum ali.
     */
    public Encontro getEncontroNaPosicaoDoHeroi() {
        for (Encontro encontro : encontros) {
            if (!encontro.isDerrotado() && encontro.getX() == getHeroiX() && encontro.getY() == getHeroiY()) {
                return encontro;
            }
        }
        return null;
    }
    
    //retorna baus que tiverem adjacente ao heroi por posiçao absoluta (checagem para interação) -- lalae
    public Bau getBauAdjacenteAoHeroi(){
        for(Bau i: baus){
            if(Math.abs(i.getX() - getHeroiX()) + Math.abs(i.getY() - getHeroiY()) == 1){
                return i;
            }
        }
        return null;
    }
    
    
    //checa se tem um bau pra onde o pesonagem vai se mover (checagem de movimento) -- lalae
    public boolean existeBau(int x, int y){
        for(Bau i: baus){
            if(i.getX() == x && i.getY() == y){
                return true;
            }
        }
        return false;
    }

    // Mesmo padrão de getBauAdjacenteAoHeroi/existeBau. Terceira cópia desse
    // tipo de loop (Encontro, Bau, Mercador): candidato a virar uma interface
    // comum (ex: Posicionavel) com um único método genérico.
    public Mercador getMercadorAdjacenteAoHeroi() {
        for (Mercador m : mercadores) {
            if (Math.abs(m.getX() - getHeroiX()) + Math.abs(m.getY() - getHeroiY()) == 1) {
                return m;
            }
        }
        return null;
    }

    public boolean existeMercador(int x, int y) {
        for (Mercador m : mercadores) {
            if (m.getX() == x && m.getY() == y) {
                return true;
            }
        }
        return false;
    }

    /** Tile (coluna) em que o herói está: o do meio da caixa de colisão. */
    public int getHeroiX() {
        return (int) Math.floor(heroiPx / TAMANHO_TILE);
    }

    /** Tile (linha) em que o herói está: o do meio da caixa de colisão. */
    public int getHeroiY() {
        return (int) Math.floor((heroiPy - ALTURA_COLISAO / 2) / TAMANHO_TILE);
    }

    /** Posição exata dos pés do herói, em pixels do mapa (usado pra desenhar). */
    public double getHeroiPx() {
        return heroiPx;
    }

    public double getHeroiPy() {
        return heroiPy;
    }

    public Mapa getMapa() { return mapa; }
    public List<Encontro> getEncontros() { return encontros; }
    public List<Bau> getBaus() { return baus; }
    public List<Mercador> getMercadores() { return mercadores; }
}
