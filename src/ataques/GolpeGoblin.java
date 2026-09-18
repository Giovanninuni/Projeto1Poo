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
		int ataque = usuario.getAtributos().getAtaque();
		return new Dano(ataque, false, TipoDano.FISICO);
	}
}
