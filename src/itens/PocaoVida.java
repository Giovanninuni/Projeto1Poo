package itens;

import entidades.Personagem;
import acoes.ResultadoAcao;

public class PocaoVida extends Item{
	private int quantidadeCura;
	
	// Package-private de proposito: só quem está no pacote itens (o
	// CatalogoDeItens) pode criar uma PocaoVida, pra não ter valores
	// divergentes criados em pontos diferentes do código (ver Masmorra
	// vs JanelaPrincipal antes dessa mudança).
	PocaoVida(String nome, String descricao, int quantidadeCura, String caminhoSprite) {
		super(nome, descricao, caminhoSprite);
		this.quantidadeCura = quantidadeCura;

	}
	
	@Override
	public ResultadoAcao consumir(Personagem usuario, Personagem alvo) {
			alvo.getVida().restaurar(quantidadeCura);
			
			return new ResultadoAcao(true, String.format("%s usou uma Poção de Cura em %s, lhe concedendo %d de Vida!%n", 
                    usuario.getNome(), alvo.getNome(), quantidadeCura));
	}
		
	}
