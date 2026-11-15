package entidades;

import ataques.GolpeGoblin;

public class Goblin extends Monstro{

	// Criados uma vez só: o mapa e a batalha pedem o sprite a cada redesenho
	private static final Sprite SPRITE = Sprite.monstro(0, 2);
	private static final SpriteMapa SPRITE_MAPA = SpriteMapa.monstro(4); // goblin

	public Goblin(String nome) {
		super(nome, 50, 12, 2, 8, 5, new GolpeGoblin());
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
