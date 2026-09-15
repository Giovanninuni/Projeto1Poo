package acoes;

import entidades.Personagem;
import habilidades.ResultadoAcao;

public interface Usavel {
	
		ResultadoAcao usar(Personagem usuario, Personagem alvo);
		
}
