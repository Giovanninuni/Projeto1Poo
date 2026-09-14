package itens;

import entidades.Heroi;

public class PocaoMana extends Item {
	private int quatidadeMana;
	
		public PocaoMana(String nome, String descricao, int quantidadeMana) {
			super(nome, descricao);
			this.quatidadeMana = quantidadeMana;
			
		}
		
		@Override
		public void usar(Heroi heroi) {
			heroi.getMana().restaurar(quatidadeMana);
		}
}
