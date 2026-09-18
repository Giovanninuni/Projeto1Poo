package ataques;

import atributos.Dano;
import entidades.Personagem;

public abstract class Ataque {
	private String nome;

	public Ataque(String nome) {
		this.nome = nome;
	}

	public abstract Dano calcularDano(Personagem usuario);

	public String getNome() {
		return nome;
	}
}
