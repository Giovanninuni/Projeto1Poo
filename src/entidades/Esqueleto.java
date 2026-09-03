package entidades;

import atributos.Dano;
import atributos.Dano.TipoDano;
import habilidades.ResultadoAcao;

public class Esqueleto extends Monstro{
	
	public Esqueleto(String nome) {
		super(nome, 70, 15, 0, 10, 8);
	}
	
	// Tipo perfurante ignora defesa
	
	@Override
	public ResultadoAcao atacar(Personagem alvo) {
		int ataque = getAtributos().getAtaque();
	    Dano dano = new Dano(ataque, false, TipoDano.PERFURANTE);
	    int danoSofrido = alvo.receberDano(dano);
	    
	    // Aqui sim acontece o retorno!
	    return new ResultadoAcao(true, String.format("O %s atacou causando " + danoSofrido + " de dano perfurante!", getNome()));
	}
}
