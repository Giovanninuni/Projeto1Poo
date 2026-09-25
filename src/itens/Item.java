package itens;

import contratos.Consumivel;
import contratos.Negociavel;
import contratos.ResultadoAcao;
import entidades.Personagem;

public abstract class Item implements Consumivel, Negociavel {
	private String nome;
	private String descricao;
	private int valor;
	private String caminhoSprite;
	

	// caminhoSprite pode ser null por enquanto -- ainda não existe sprite de
	// item de verdade, o campo só está pronto pra quando existir (mesmo
	// padrão usado em mundo.TipoTile antes dos sprites de tile existirem).
	public Item(String nome, String descricao, int valor, String caminhoSprite) {
		this.nome = nome;
		this.descricao = descricao;
		this.valor = valor;
		this.caminhoSprite = caminhoSprite;
	}

	public abstract ResultadoAcao consumir(Personagem usuario, Personagem alvo);

	@Override
	public String getNome() {
		return this.nome;
	}

	public String getDescricao() {
		return this.descricao;
	}
	
	@Override
	public int getValor() {
		return this.valor;
	}

	public String getCaminhoSprite() {
		return this.caminhoSprite;
	}
}
