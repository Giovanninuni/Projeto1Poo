package habilidades;

import atributos.Dano;
import atributos.Dano.TipoDano;
import entidades.Personagem;

public class GolpeEspada extends Habilidade {
	
	public GolpeEspada() {
		super("Golpe de Espada", 0); //Sem custo de mana
	}
	
	@Override
	public ResultadoAcao executar(Personagem usuario, Personagem alvo) {
		int ataqueBase = usuario.getAtributos().getAtaque();
		
		Dano dano = gerarDanoComCritico(ataqueBase, 25, TipoDano.FISICO);
		
		int danoSofrido = alvo.receberDano(dano);
		
        String mensagem;
        if(dano.isCritico()) {
			mensagem = String.format("ACERTO CRÍTICO! %s desfere um golpe devastador de espada!%n", usuario.getNome(), alvo.getNome(), danoSofrido);
		}
        else {
        	mensagem = String.format("%s desferiu um golpe de espada em %s causando %d de dano.%n", usuario.getNome(), alvo.getNome(), danoSofrido);
        }
        
        return new ResultadoAcao(true, mensagem);
	}
}
