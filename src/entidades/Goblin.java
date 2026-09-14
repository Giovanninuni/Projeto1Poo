package entidades;

import atributos.Dano;
import atributos.Dano.TipoDano;
import habilidades.ResultadoAcao;

public class Goblin extends Monstro{
	
	public Goblin(String nome) {
		super(nome, 50, 12, 2, 8, 5);
	}
	
	@Override
	public ResultadoAcao atacar(Personagem alvo) {
	    int ataque = getAtributos().getAtaque();
	    Dano dano = new Dano(ataque, false, TipoDano.FISICO);
	    int danoSofrido = alvo.receberDano(dano);
	    
	    return new ResultadoAcao(true, String.format("O %s atacou causando " + danoSofrido + " de dano !", getNome()));
	}
}
