package atributos;

public class Ouro {
	private int quantidade;

	public Ouro() {
		this.quantidade = 0;
	}

	public void adicionar(int valor) {
		this.quantidade += valor;
	}

	public int getQuantidade() {
		return quantidade;
	}
}
