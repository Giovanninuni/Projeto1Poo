package atributos;

public class Experiencia {
	private int xpAtual;
	private int nivel;

	public Experiencia() {
		this.xpAtual = 0;
		this.nivel = 1;
	}

	// Devolve quantos níveis isso rendeu (pode ser 0, 1, ou mais de 1)
	public int ganhar(int quantidade) {
		this.xpAtual += quantidade;
		int niveisGanhos = 0;

		while (xpAtual >= custoProximoNivel()) {
			xpAtual -= custoProximoNivel();
			nivel++;
			niveisGanhos++;
		}

		return niveisGanhos;
	}

	private int custoProximoNivel() {
		return nivel * 50; // provisório, balancear depois
	}

	public int getNivel() {
		return nivel;
	}

	public int getXpAtual() {
		return xpAtual;
	}
}
