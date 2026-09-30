package ataques;

import atributos.Dano;
import entidades.Personagem;

public abstract class Tecnica extends Ataque {
	private int custoMana;

	public Tecnica(String nome, int custoMana) {
		super(nome);
		this.custoMana = custoMana;
	}

	@Override
	public abstract Dano calcularDano(Personagem usuario);

	public int getCustoMana() {
		return custoMana;
	}
}
