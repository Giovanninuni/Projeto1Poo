package core;

import entidades.Heroi;
import entidades.Monstro;
import habilidades.ResultadoAcao;
import java.util.List;

public class Combate {
    
    private List<Heroi> herois;
    private List<Monstro> monstros;
    
    private int indiceHeroiAtual = 0; 
    private boolean combateAtivo = true; 

    public Combate(List<Heroi> grupoHerois, List<Monstro> grupoMonstros) {
        this.herois = grupoHerois;
        this.monstros = grupoMonstros;
        this.indiceHeroiAtual = 0;
        this.combateAtivo = true;
    }

    public void atacarAlvoSelecionado(int indiceMonstroClicado) {
        if (!combateAtivo) return;

        Heroi atacante = getHeroiAtual();
        if (atacante == null) return; 

        Monstro alvo = this.monstros.get(indiceMonstroClicado);
        
        if (!alvo.estaVivo()) {
            System.out.println("Este monstro já foi derrotado! Escolha outro alvo.");
            return; 
        }
        
        // Delegação da lógica de dano encapsulada na classe dos personagens
        atacante.atacar(alvo);
        
        if (verificarVitoria()) {
            this.combateAtivo = false;
            System.out.println("Vitória! Todos os monstros foram derrotados.");
            return;
        }
        
        avancarTurno();
    }

    private void avancarTurno() {
        if (verificarDerrota()) return; // Trava de segurança inicial

        indiceHeroiAtual++;
        
        // Pula o turno de heróis que já não estão vivos
        while (indiceHeroiAtual < herois.size() && !herois.get(indiceHeroiAtual).estaVivo()) {
            indiceHeroiAtual++;
        }
        
        if (indiceHeroiAtual >= herois.size()) {
            turnoDosMonstros();
            
            if (verificarDerrota()) return; // Trava de segurança pós-ataque inimigo
            
            indiceHeroiAtual = 0;
            while (indiceHeroiAtual < herois.size() && !herois.get(indiceHeroiAtual).estaVivo()) {
                indiceHeroiAtual++;
            }
        }
    }
    
    private void turnoDosMonstros() {
        if (!combateAtivo) return;

        for (Monstro monstro : monstros) {
            if (!monstro.estaVivo()) continue; 
            
            Heroi alvoSorteado = sortearHeroiVivo();
            
            if (alvoSorteado != null) {
                // O monstro também utiliza o método padrão para causar dano
                monstro.atacar(alvoSorteado);
                System.out.println(monstro.getNome() + " atacou " + alvoSorteado.getNome());
            }
        }
        
        if (verificarDerrota()) {
            this.combateAtivo = false;
            System.out.println("Derrota! O esquadrão caiu em batalha.");
        }
    }

    private Heroi sortearHeroiVivo() {
        for (Heroi h : herois) {
            if (h.estaVivo()) {
                return h; 
            }
        }
        return null; 
    }

    public boolean verificarVitoria() {
        for (Monstro m : monstros) {
            if (m.estaVivo()) return false; 
        }
        return true;
    }

    public boolean verificarDerrota() {
        for (Heroi h : herois) {
            if (h.estaVivo()) return false; 
        }
        return true;
    }

    public List<Heroi> getHerois() { return herois; }
    public List<Monstro> getMonstros() { return monstros; }
    
    public Heroi getHeroiAtual() { 
        // Impede que o sistema procure um índice inválido quando a party morre
        if (indiceHeroiAtual >= herois.size()) return null;
        return herois.get(indiceHeroiAtual); 
    }
    
    public boolean isCombateAtivo() { return combateAtivo; }

    // ==========================================
    // --- MÉTODOS EXIGIDOS PELO PAINELCOMBATE ---
    // ==========================================

    public boolean batalhaAtiva() {
        return this.isCombateAtivo();
    }

    public String verificarVencedor() {
        if (verificarVitoria()) return "Vitória! Inimigos derrotados.";
        if (verificarDerrota()) return "Derrota! A party foi aniquilada.";
        return "Batalha em andamento...";
    }

    public ResultadoAcao processarAcaoHeroiHabilidade(int indiceHeroi, int indiceHabilidade, int indiceAlvo) {
        if (!isCombateAtivo()) return new ResultadoAcao(false, "A batalha já acabou.");
        
        Heroi atacante = this.herois.get(indiceHeroi);
        Monstro alvo = this.monstros.get(indiceAlvo);
        
        if (!alvo.estaVivo()) { 
            return new ResultadoAcao(false, "O alvo já está derrotado!");
        }

        ResultadoAcao resultado = atacante.usarHabilidade(indiceHabilidade, alvo);
        
        if (verificarVitoria()) {
            this.combateAtivo = false;
        } else if (resultado.isSucesso()) {
            avancarTurno(); 
        }
        
        return resultado;
    }

    public ResultadoAcao processarAcaoHeroiItem(int indiceHeroi, int indiceItem) {
        if (!isCombateAtivo()) return new ResultadoAcao(false, "A batalha já acabou.");
        
        Heroi consumidor = this.herois.get(indiceHeroi);
        ResultadoAcao resultado = consumidor.usarItem(indiceItem);
        
        if (resultado.isSucesso()) avancarTurno();
        
        return resultado;
    }

    public ResultadoAcao processarTurnoInimigos() {
        StringBuilder logInimigos = new StringBuilder();
        
        for (Monstro monstro : monstros) {
            if (!monstro.estaVivo()) continue; 
            
            Heroi alvoSorteado = sortearHeroiVivo();
            if (alvoSorteado != null) {
                ResultadoAcao acao = monstro.atacar(alvoSorteado);
                logInimigos.append(acao.getMensagem()).append("\n");
            }
        }
        
        if (verificarDerrota()) this.combateAtivo = false;
        
        return new ResultadoAcao(true, logInimigos.toString());
    }
}