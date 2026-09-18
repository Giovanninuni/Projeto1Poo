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

	// Usados ao trocar/remover equipamento: remove o bonus do item antigo
	// antes de aplicar o do novo, ou ao desequipar de vez (ver equipamentos.Equipagem).
	public void reduzirAtaque(int quantidade) {
		this.ataque -= quantidade;
	}

	public void reduzirDefesa(int quantidade) {
		this.defesa -= quantidade;
	}
}
