package ataques;

import atributos.Dano;
import atributos.Dano.TipoDano;
import entidades.Personagem;

public class GolpeEspada extends Habilidade {

	public GolpeEspada() {
		super("Golpe de Espada", 0); //Sem custo de mana
	}

	@Override
	public Dano calcularDano(Personagem usuario) {
		int ataqueBase = usuario.getAtributos().getAtaque();
		return gerarDanoComCritico(ataqueBase, 25, TipoDano.FISICO);
	}
}
