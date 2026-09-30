package ataques;

import atributos.Dano;
import atributos.Dano.TipoDano;
import entidades.Personagem;

public class Ciclone extends Tecnica {

	public Ciclone() {
		super("Ciclone", 10);
	}

	@Override
	public Dano calcularDano(Personagem usuario) {
		int poder = usuario.getAtributos().getPoder() + 8;
		return gerarDanoComCritico(poder, 30, TipoDano.FISICO);
	}
}
