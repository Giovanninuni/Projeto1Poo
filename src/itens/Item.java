package itens;

import entidades.Personagem;
import acoes.Consumivel;
import acoes.ResultadoAcao;

public abstract class Item implements Consumivel {
	private String nome;
	private String descricao;

	public Item(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}

	public abstract ResultadoAcao consumir(Personagem usuario, Personagem alvo);
	
	public String getNome() {
		return this.nome;	
	}
	
	public String getDescricao() {
		return this.descricao;
	}
}
