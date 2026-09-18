package ataques;

import java.util.Random;

import atributos.Dano;
import atributos.Dano.TipoDano;
import entidades.Personagem;

public abstract class Habilidade extends Ataque {
	private int custoMana;
	private static final Random sorteador = new Random();

	public Habilidade(String nome, int custoMana) {
		super(nome);
		this.custoMana = custoMana;
	}

	@Override
	public abstract Dano calcularDano(Personagem usuario);

	protected Dano gerarDanoComCritico(int ataqueBase, int chanceCritico, TipoDano tipo) {
        boolean critico = sorteador.nextInt(100) < chanceCritico;
        int danoFinal = ataqueBase;
        if (critico) {
            danoFinal = (int) (ataqueBase * 1.5); // Aumenta em 50%
        }

        return new Dano(danoFinal, critico, tipo);
    }

	public int getCustoMana() {
		return custoMana;
	}
}
