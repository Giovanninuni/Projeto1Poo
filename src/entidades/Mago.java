package entidades;

import ataques.ToqueArcano;
import ataques.BolaDeFogo;

public class Mago extends Heroi {

	// Criados uma vez só: o mapa e a batalha pedem o sprite a cada redesenho
	private static final Sprite SPRITE = Sprite.rogue(4, 1); // mago de barba azul
	private static final SpriteMapa SPRITE_MAPA = SpriteMapa.personagem(2); // encapuzado azul

	public Mago(String nome, int vidaMaxima, int poderBase, int defesa, int manaMaxima) {
		super(nome, vidaMaxima, poderBase, defesa, manaMaxima, new ToqueArcano());
		aprenderTecnicas(new BolaDeFogo());
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
