package itens;

import entidades.Heroi;

public class PocaoVida extends Item{
	private int quantidadeCura;
	
	public PocaoVida(String nome, String descricao, int quantidadeCura) {
		super(nome, descricao);
		this.quantidadeCura = quantidadeCura;
		
	}
	
	@Override
	public void usar(Heroi heroi) {
		heroi.getVida().restaurar(quantidadeCura);
	}
}
