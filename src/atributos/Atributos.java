package atributos;

public class Atributos {
	private int poder;
	private int defesa;
	
	public Atributos(int poder, int defesa) {
		this.poder = poder;
		this.defesa = defesa;
	}
	
	public int getPoder() {
		return this.poder;
	}
	
	public int getDefesa() {
		return this.defesa;
	}

	public void aumentarPoder(int quantidade) {
		this.poder += quantidade;
	}

	public void aumentarDefesa(int quantidade) {
		this.defesa += quantidade;
	}

	// Usados ao trocar/remover equipamento: remove o bonus do item antigo
	// antes de aplicar o do novo, ou ao desequipar de vez (ver equipamentos.Equipagem).
	public void reduzirPoder(int quantidade) {
		this.poder -= quantidade;
	}

	public void reduzirDefesa(int quantidade) {
		this.defesa -= quantidade;
	}
}
