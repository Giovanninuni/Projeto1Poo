package entidades;

import ataques.GolpeEsqueleto;

public class Esqueleto extends Monstro{

	// Criados uma vez só: o mapa e a batalha pedem o sprite a cada redesenho
	private static final Sprite SPRITE = Sprite.monstro(4, 0);
	private static final SpriteMapa SPRITE_MAPA = SpriteMapa.monstro(3); // esqueleto

	public Esqueleto(String nome) {
		super(nome, 70, 15, 0, 10, 8, new GolpeEsqueleto());
	}

	@Override
	public Sprite getSprite() {
		return SPRITE;
	}

	@Override
	public SpriteMapa getSpriteMapa() {
		return SPRITE_MAPA;
	}
}
