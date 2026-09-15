package itens;

import entidades.Personagem;
import acoes.Usavel;
import habilidades.ResultadoAcao;

public abstract class Item implements Usavel {
	private String nome;
	private String descricao;
	
	public Item(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}
	
	public abstract ResultadoAcao usar(Personagem usuario, Personagem alvo);
	
	public String getNome() {
		return this.nome;	
	}
	
	public String getDescricao() {
		return this.descricao;
	}
}
