package habilidades;

import atributos.Dano;
import atributos.Dano.TipoDano;
import entidades.Heroi;
import entidades.Personagem;


public class BolaDeFogo extends Habilidade{
	public BolaDeFogo() {
		super("Bola de Fogo", 7);
	}
	
	@Override
	public ResultadoAcao executar(Personagem usuario, Personagem alvo) {
		if(usuario instanceof Heroi heroi) { //checa e faz o cast para heroi
			if (heroi.getMana().gastar(getCustoMana())) {	
			int poder = usuario.getAtributos().getAtaque() + 10;
			Dano dano = new Dano(poder, false, TipoDano.MAGICO);
			int danoSofrido = alvo.receberDano(dano);
			
			return new ResultadoAcao(true, String.format("%s conjurou Bola de Fogo em %s causando %d de dano mágico!%n", 
                    heroi.getNome(), alvo.getNome(), danoSofrido));
			}
			
			else {
				return new ResultadoAcao(false, 
		                String.format("%s não tem mana suficiente para conjurar %s!", 
		                    heroi.getNome(), getNome()));
			}
		}
		return new ResultadoAcao(false, "Usuário inválido para esta habilidade.");
	}
}
