package itens;

import entidades.Personagem;
import acoes.ResultadoAcao;

public class PocaoVida extends Item{
	private int quantidadeCura;
	
	public PocaoVida(String nome, String descricao, int quantidadeCura) {
		super(nome, descricao);
		this.quantidadeCura = quantidadeCura;
		
	}
	
	@Override
	public ResultadoAcao consumir(Personagem usuario, Personagem alvo) {
			alvo.getVida().restaurar(quantidadeCura);
			
			return new ResultadoAcao(true, String.format("%s usou uma Poção de Cura em %s, lhe concedendo %d de Vida!%n", 
                    usuario.getNome(), alvo.getNome(), quantidadeCura));
	}
		
	}
