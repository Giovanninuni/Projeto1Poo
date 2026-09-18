package ataques;

import atributos.Dano;
import atributos.Dano.TipoDano;
import entidades.Personagem;

public class Ciclone extends Habilidade {

	public Ciclone() {
		super("Ciclone", 10);
	}

	@Override
	public Dano calcularDano(Personagem usuario) {
		int ataqueBase = usuario.getAtributos().getAtaque() + 8;
		return gerarDanoComCritico(ataqueBase, 30, TipoDano.FISICO);
	}
}
