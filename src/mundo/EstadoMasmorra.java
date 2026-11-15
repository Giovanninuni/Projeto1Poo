package mundo;

import java.util.List;

import objetos.Bau;
import objetos.Mercador;
import objetos.Porta;
import util.ContadorObjetos;

/**
 * Agrupa tudo o que a Masmorra precisa receber para ser montada: onde o
 * herói começa e o que está espalhado pelo mapa (baús, encontros e
 * mercadores, cada um com o seu estado: aberto/fechado, derrotado...).
 *
 * Quem cria este objeto decide o conteúdo: hoje é a FabricaDeJogoNovo
 * (partida nova); depois, o carregador do save vai montar um igual a
 * partir do arquivo. A Masmorra não precisa saber de onde ele veio.
 */
public class EstadoMasmorra {

    {
        ContadorObjetos.registrar(this);
    }

    private final int heroiTileX;
    private final int heroiTileY;
    private final List<Bau> baus;
    private final List<Masmorra.Encontro> encontros;
    private final List<Mercador> mercadores;
    private final List<Porta> portas;

    public EstadoMasmorra(int heroiTileX, int heroiTileY, List<Bau> baus,
            List<Masmorra.Encontro> encontros, List<Mercador> mercadores, List<Porta> portas) {
        this.heroiTileX = heroiTileX;
        this.heroiTileY = heroiTileY;
        this.baus = baus;
        this.encontros = encontros;
        this.mercadores = mercadores;
        this.portas = portas;
    }

    public int getHeroiTileX() {
        return this.heroiTileX;
    }

    public int getHeroiTileY() {
        return this.heroiTileY;
    }

    public List<Bau> getBaus() {
        return this.baus;
    }
    
    public List<Porta> getPortas(){
        return this.portas;
    }

    public List<Masmorra.Encontro> getEncontros() {
        return this.encontros;
    }

    public List<Mercador> getMercadores() {
        return this.mercadores;
    }
}
