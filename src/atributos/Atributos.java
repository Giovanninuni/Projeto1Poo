package atributos;

public class Atributos {
	private int ataque;
	private int defesa;
	
	public Atributos(int ataque, int defesa) {
		this.ataque = ataque;
		this.defesa = defesa;
	}
	
	public int getAtaque() {
		return this.ataque;
	}
	
	public int getDefesa() {
		return this.defesa;
	}

	public void aumentarAtaque(int quantidade) {
		this.ataque += quantidade;
	}

	public void aumentarDefesa(int quantidade) {
		this.defesa += quantidade;
	}
}
