package core;

import entidades.Personagem;
import entidades.Heroi;
import habilidades.ResultadoAcao;

public class Combate {
    private Heroi heroi;
    private Personagem inimigo;
    
    public Combate(Heroi heroi, Personagem inimigo) {
        this.heroi = heroi;
        this.inimigo = inimigo;
    }
    
    // Método chamado pelo Swing quando o jogador clica no botão de Atacar/Magia
    public ResultadoAcao processarAcaoHeroiHabilidade(int indiceHabilidade) {
        if (!batalhaAtiva()) {
            return new ResultadoAcao(false, "A batalha já terminou!");
        }
        // O herói age e devolvemos o texto do que aconteceu
        return heroi.usarHabilidade(indiceHabilidade, inimigo);
    }
    
    // Método chamado pelo Swing quando o jogador clica para usar um Item da mochila
    public ResultadoAcao processarAcaoHeroiItem(int indiceItem) {
        if (!batalhaAtiva()) {
            return new ResultadoAcao(false, "A batalha já terminou!");
        }
        return heroi.usarItem(indiceItem);
    }
    
    // Método chamado logo após o herói agir com sucesso
    public ResultadoAcao processarTurnoInimigo() {
        if (inimigo.estaVivo()) {
            return inimigo.atacar(heroi);
        }
        return new ResultadoAcao(false, inimigo.getNome() + " já está derrotado e não pode agir.");
    }
    
    // Verifica se os dois ainda estão vivos para continuar o combate
    public boolean batalhaAtiva() {
        return heroi.estaVivo() && inimigo.estaVivo();
    }
    
    // Verifica quem foi o vencedor
    public String verificarVencedor() {
        if (heroi.estaVivo() && !inimigo.estaVivo()) {
            return "Vitória! Você derrotou o " + inimigo.getNome() + "!";
        } else if (!heroi.estaVivo()) {
            return "Derrota! Você tombou em batalha...";
        }
        return "A batalha ainda está acontecendo.";
    }

    // Getters para a interface gráfica poder desenhar as barras de vida
    public Heroi getHeroi() {
        return heroi;
    }

    public Personagem getInimigo() {
        return inimigo;
    }
}