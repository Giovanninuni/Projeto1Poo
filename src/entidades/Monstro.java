package entidades;

import ataques.Ataque;

public abstract class Monstro extends Personagem{
	private int xpConcedida;
	private int ouroDropado;
	private Ataque ataque;

	public Monstro(String nome, int vidaMaxima, int ataqueBase, int defesa, int xpConcedida, int ouroDropado, Ataque ataque) {
		super(nome, vidaMaxima, ataqueBase, defesa);
		this.xpConcedida = xpConcedida;
		this.ouroDropado = ouroDropado;
		this.ataque = ataque;
	}

	public Ataque getAtaque() {
		return this.ataque;
	}

	public int getXpConcedida(){
		return this.xpConcedida;
	}

	public int getOuroDropado() {
		return this.ouroDropado;
	}
}
