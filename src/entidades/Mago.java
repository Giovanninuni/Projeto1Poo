package entidades;

import ataques.ToqueArcano;
import ataques.BolaDeFogo;

public class Mago extends Heroi {

	public Mago(String nome, int vidaMaxima, int ataqueBase, int defesa, int manaMaxima) {
		super(nome, vidaMaxima, ataqueBase, defesa, manaMaxima, new ToqueArcano());
		aprenderHabilidade(new BolaDeFogo());
	}

	@Override
	public Sprite getSprite() {
		return Sprite.rogue(4, 1); // mago de barba azul
	}
}
