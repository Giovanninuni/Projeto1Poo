package entidades;

import ataques.ToqueArcano;
import ataques.BolaDeFogo;

public class Mago extends Heroi {

	public Mago(String nome, int vidaMaxima, int poderBase, int defesa, int manaMaxima) {
		super(nome, vidaMaxima, poderBase, defesa, manaMaxima, new ToqueArcano());
		aprenderTecnicas(new BolaDeFogo());
	}

	@Override
	public Sprite getSprite() {
		return Sprite.rogue(4, 1); // mago de barba azul
	}

	@Override
	public SpriteMapa getSpriteMapa() {
		return SpriteMapa.personagem(2); // encapuzado azul
	}
}
