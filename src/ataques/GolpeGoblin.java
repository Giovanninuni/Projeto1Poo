package ataques;

import atributos.Dano;
import atributos.Dano.TipoDano;
import entidades.Personagem;

public class GolpeGoblin extends Ataque {
	public GolpeGoblin() {
		super("Golpe de Goblin");
	}

	@Override
	public Dano calcularDano(Personagem usuario) {
		int poder = usuario.getAtributos().getPoder();
		return new Dano(poder, false, TipoDano.FISICO);
	}
}
