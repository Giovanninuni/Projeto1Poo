package ataques;

import atributos.Dano;
import atributos.Dano.TipoDano;
import entidades.Personagem;

public class GolpeEsqueleto extends Ataque {
	public GolpeEsqueleto() {
		super("Golpe de Esqueleto");
	}

	@Override
	public Dano calcularDano(Personagem usuario) {
		int ataque = usuario.getAtributos().getAtaque();
		return new Dano(ataque, false, TipoDano.PERFURANTE);
	}
}
