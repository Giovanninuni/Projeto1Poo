package ataques;

import java.util.Random;

import atributos.Dano;
import atributos.Dano.TipoDano;
import entidades.Personagem;

public abstract class Ataque {
	private String nome;
	private static final Random sorteador = new Random();

	public Ataque(String nome) {
		this.nome = nome;
	}

	public abstract Dano calcularDano(Personagem usuario);

	protected Dano gerarDanoComCritico(int poderBase, int chanceCritico, TipoDano tipo) {
        boolean critico = sorteador.nextInt(100) < chanceCritico;
        int danoFinal = poderBase;
        if (critico) {
            danoFinal = (int) (poderBase * 1.5); // Aumenta em 50%
        }

        return new Dano(danoFinal, critico, tipo);
    }

	public String getNome() {
		return nome;
	}
}
