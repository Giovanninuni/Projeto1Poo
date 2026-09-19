package itens;

import entidades.Heroi;
import entidades.Personagem;
import acoes.ResultadoAcao;

public class PocaoMana extends Item {
	private int quantidadeMana;
	
		// Package-private de proposito: só quem está no pacote itens (o
		// CatalogoDeItens) pode criar uma PocaoMana, pra não ter valores
		// divergentes criados em pontos diferentes do código (ver Masmorra
		// vs JanelaPrincipal antes dessa mudança).
		PocaoMana(String nome, String descricao, int quantidadeMana, String caminhoSprite) {
			super(nome, descricao, caminhoSprite);
			this.quantidadeMana = quantidadeMana;

		}
		
		@Override
		public ResultadoAcao consumir(Personagem usuario, Personagem alvo) {
			if(usuario instanceof Heroi heroiUsuario && alvo instanceof Heroi heroiAlvo) {
			heroiAlvo.getMana().restaurar(quantidadeMana);
			
			return new ResultadoAcao(true, String.format("%s usou uma Poção de Mana em %s, lhe concedendo %d de Mana!%n", 
                    heroiUsuario.getNome(), heroiAlvo.getNome(), quantidadeMana));
			}
			
			else {

				return new ResultadoAcao(false, "Essa poção só pode ser usada em um herói.");
			}
			
			
			
		}
}
