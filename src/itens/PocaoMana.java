package itens;

import entidades.Heroi;

public class PocaoMana extends Item {
	private int quantidadeMana;
	
		public PocaoMana(String nome, String descricao, int quantidadeMana) {
			super(nome, descricao);
			this.quantidadeMana = quantidadeMana;
			
		}
		
		@Override
		public void usar(Heroi heroi) {
			heroi.getMana().restaurar(quantidadeMana);
		}
}
