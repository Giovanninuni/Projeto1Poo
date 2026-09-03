package itens;

import entidades.Heroi;

public class PocaoVida extends Item{
	private int quatidadeCura;
	
	public PocaoVida(String nome, String descricao, int quantidadeCura) {
		super(nome, descricao);
		this.quatidadeCura = quantidadeCura;
		
	}
	
	@Override
	public void usar(Heroi heroi) {
		heroi.getVida().restaurar(quatidadeCura);
	}
}
