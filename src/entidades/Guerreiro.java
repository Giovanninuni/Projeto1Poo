package entidades;

import ataques.GolpeEspada;
import ataques.Ciclone;

public class Guerreiro extends Heroi {

	// Criados uma vez só: o mapa e a batalha pedem o sprite a cada redesenho
	private static final Sprite SPRITE = Sprite.rogue(1, 0); // cavaleiro
	private static final SpriteMapa SPRITE_MAPA = SpriteMapa.personagem(1); // cavaleiro

	public Guerreiro(String nome, int vidaMaxima, int poderBase, int defesa, int manaMaxima) {
		super(nome, vidaMaxima, poderBase, defesa, manaMaxima, new GolpeEspada());
		aprenderTecnicas(new Ciclone());
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
