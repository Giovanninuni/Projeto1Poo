package entidades;

import atributos.Ouro;
import java.util.List;

// Estado persistente do grupo do jogador — sobrevive entre a exploração e
// os combates. Fica em entidades, não na GUI, porque é estado de domínio:
// a janela só exibe, não é dona dele.
public class Grupo {
	private List<Heroi> herois;
	private Ouro ouro;

	public Grupo(List<Heroi> herois) {
		this.herois = herois;
		this.ouro = new Ouro();
	}

	public List<Heroi> getHerois() {
		return herois;
	}

	public Ouro getOuro() {
		return ouro;
	}
}
