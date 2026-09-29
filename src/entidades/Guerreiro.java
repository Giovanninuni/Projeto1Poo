package entidades;

import ataques.GolpeEspada;
import ataques.Ciclone;

public class Guerreiro extends Heroi {

	public Guerreiro(String nome, int vidaMaxima, int ataqueBase, int defesa, int manaMaxima) {
		super(nome, vidaMaxima, ataqueBase, defesa, manaMaxima, new GolpeEspada());
		aprenderHabilidade(new Ciclone());
	}

	@Override
	public Sprite getSprite() {
		return Sprite.rogue(1, 0); // cavaleiro
	}

	@Override
	public SpriteMapa getSpriteMapa() {
		return SpriteMapa.personagem(1); // cavaleiro
	}
}
