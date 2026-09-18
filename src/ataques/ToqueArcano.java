package ataques;

import atributos.Dano;
import atributos.Dano.TipoDano;
import entidades.Personagem;

public class ToqueArcano extends Ataque {

	public ToqueArcano() {
		super("Toque Arcano");
	}

	@Override
	public Dano calcularDano(Personagem usuario) {
		int poder = usuario.getAtributos().getAtaque() + 3;
		return gerarDanoComCritico(poder, 15, TipoDano.MAGICO);
	}
}
