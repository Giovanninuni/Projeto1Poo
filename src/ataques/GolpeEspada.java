package ataques;

import atributos.Dano;
import atributos.Dano.TipoDano;
import entidades.Personagem;

public class GolpeEspada extends Ataque {

	public GolpeEspada() {
		super("Golpe de Espada");
	}

	@Override
	public Dano calcularDano(Personagem usuario) {
		int poder = usuario.getAtributos().getPoder();
		return gerarDanoComCritico(poder, 25, TipoDano.FISICO);
	}
}
