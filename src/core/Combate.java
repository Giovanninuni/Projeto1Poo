	package core;

import entidades.Personagem;
import entidades.Heroi;
import habilidades.ResultadoAcao;
import java.util.List;

public class Combate {
    private List<Heroi> herois;
    private List<Personagem> inimigos;
    
    public Combate(List<Heroi> herois, List<Personagem> inimigos) {
        this.herois = herois;
        this.inimigos = inimigos;
    }	
    
    
    
    // Método chamado pelo Swing quando o jogador clica no botão de Atacar/Magia
    public ResultadoAcao processarAcaoHeroiHabilidade(int indiceHeroi, int indiceHabilidade, int indiceAlvo) {
        if (!batalhaAtiva()) {
            return new ResultadoAcao(false, "A batalha já terminou!");
        }
        
        Heroi atacante = herois.get(indiceHeroi);
        Personagem alvo = inimigos.get(indiceAlvo);
        
        if (!atacante.estaVivo()) return new ResultadoAcao(false, atacante.getNome() + " está desmaiado!");
        if (!alvo.estaVivo()) return new ResultadoAcao(false, alvo.getNome() + " já está derrotado!");
        
        return atacante.usarHabilidade(indiceHabilidade, alvo);
    }
    
    // Método chamado pelo Swing quando o jogador clica para usar um Item da mochila
    public ResultadoAcao processarAcaoHeroiItem(int indiceHeroi, int indiceItem) {
        if (!batalhaAtiva()) {
            return new ResultadoAcao(false, "A batalha já terminou!");
        }
        return herois.get(indiceHeroi).usarItem(indiceItem);
    }
    
    // Método chamado logo após o herói agir com sucesso
    public ResultadoAcao processarTurnoInimigos() {
    	StringBuilder relatorioTurno = new StringBuilder();

    	for(Personagem inimigo : inimigos) {
    		if(inimigo.estaVivo() && timeHeroisVivo()) {
    			
    			// Por enquanto, o inimigo sempre ataca o primeiro herói vivo que encontrar
                Heroi alvo = null;
                for (Heroi h : herois) {
                    if (h.estaVivo()) {
                        alvo = h;
                        break;
                    }
                }
                ResultadoAcao acao = inimigo.atacar(alvo);
                relatorioTurno.append(acao.getMensagem()).append("\n  ");
    		}
    	}
    	return new ResultadoAcao(true, relatorioTurno.toString().trim());
    }
    
    // Verifica se os dois ainda estão vivos para continuar o combate
    public boolean batalhaAtiva() {
        return 	timeHeroisVivo() && timeInimigosVivo();		
    }
    
    // Verifica quem foi o vencedor
    public String verificarVencedor() {
        if (timeHeroisVivo() && !timeInimigosVivo()) {
        	if(inimigos.size() == 1) { // Se so tiver 1 heroi
        		return "Vitória! Você derrotou o " + inimigos.get(0).getNome() + "!";
        	}
            return "Vitória! Você derrotou todos os inimigos!";
        } else if (!timeHeroisVivo()) {
        	if(herois.size() == 1) { // Se so tiver 1 inimigo
        		return "Derrota! " + herois.get(0).getNome() + " tombou em batalha...";
        	}
            return "Derrota! Toda a sua equipe tombou em batalha...";
            
        }
        return "A batalha ainda está acontecendo.";
    }
    
    public boolean timeHeroisVivo() {
        for (Heroi h : herois) {
            if (h.estaVivo()) return true;
        }
        return false;
    }
    
    public boolean timeInimigosVivo() {
        for (Personagem i : inimigos) {
            if (i.estaVivo()) return true;
        }
        return false;
    }

    // Getters para a interface gráfica poder desenhar as barras de vida
    public List<Heroi> getHerois() {
        return herois;
    }

    public List<Personagem> getInimigos() {
        return inimigos;
    }
}