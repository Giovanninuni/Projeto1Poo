package itens;

import entidades.Personagem;
import acoes.Consumivel;
import acoes.ResultadoAcao;

public abstract class Item implements Consumivel {
	private String nome;
	private String descricao;
	private String caminhoSprite;

	// caminhoSprite pode ser null por enquanto -- ainda não existe sprite de
	// item de verdade, o campo só está pronto pra quando existir (mesmo
	// padrão usado em mundo.TipoTile antes dos sprites de tile existirem).
	public Item(String nome, String descricao, String caminhoSprite) {
		this.nome = nome;
		this.descricao = descricao;
		this.caminhoSprite = caminhoSprite;
	}

	public abstract ResultadoAcao consumir(Personagem usuario, Personagem alvo);

	public String getNome() {
		return this.nome;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public String getCaminhoSprite() {
		return this.caminhoSprite;
	}
}
