package atributos;

public class Ouro {
	private int quantidade;

	public Ouro() {
		this.quantidade = 0;
	}

	public void adicionar(int valor) {
		this.quantidade += valor;
	}

	// Espelha Mana.gastar(): o próprio Ouro garante que nunca fica negativo,
	// em vez de quem chama precisar checar getQuantidade() antes. Ainda sem
	// uso (preparado pra quando a loja existir).
	public boolean gastar(int valor) {
		if (valor > 0 && valor <= quantidade) {
			quantidade -= valor;
			return true;
		}
		return false;
	}

	public int getQuantidade() {
		return quantidade;
	}
}
