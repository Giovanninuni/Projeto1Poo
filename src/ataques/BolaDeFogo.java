package ataques;

import atributos.Dano;
import atributos.Dano.TipoDano;
import entidades.Personagem;

public class BolaDeFogo extends Habilidade {
	public BolaDeFogo() {
		super("Bola de Fogo", 7);
	}

	@Override
	public Dano calcularDano(Personagem usuario) {
		int poder = usuario.getAtributos().getAtaque() + 10;
		return new Dano(poder, false, TipoDano.MAGICO);
	}
}
