package entidades;

import ataques.GolpeGoblin;

public class Goblin extends Monstro{

	public Goblin(String nome) {
		super(nome, 50, 12, 2, 8, 5, new GolpeGoblin());
	}

	@Override
	public Sprite getSprite() {
		return Sprite.monstro(0, 2);
	}
}
