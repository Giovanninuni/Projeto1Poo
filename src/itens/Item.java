package itens;

import entidades.Heroi;

public abstract class Item {
	private String nome;
	private String descricao;
	
	public Item(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}
	
	public abstract void usar(Heroi heroi);
	
	public String getNome() {
		return this.nome;	
	}
	
	public String getDescricao() {
		return this.descricao;
	}
}
