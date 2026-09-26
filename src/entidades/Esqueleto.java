package entidades;

import ataques.GolpeEsqueleto;

public class Esqueleto extends Monstro{

	public Esqueleto(String nome) {
		super(nome, 70, 15, 0, 10, 8, new GolpeEsqueleto());
	}

	@Override
	public Sprite getSprite() {
		return Sprite.monstro(4, 0);
	}
}
