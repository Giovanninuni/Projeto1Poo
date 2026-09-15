package core;

import entidades.Heroi;
import entidades.Monstro;
import habilidades.ResultadoAcao;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Combate {

    private List<Heroi> herois;
    private List<Monstro> monstros;

    private int indiceHeroiAtual = 0;
    private boolean combateAtivo = true;
    private final Random sorteador = new Random();

    public Combate(List<Heroi> grupoHerois, List<Monstro> grupoMonstros) {
        this.herois = grupoHerois;
        this.monstros = grupoMonstros;
        this.indiceHeroiAtual = 0;
        this.combateAtivo = true;
    }

    // ==========================================
    // --- CICLO DE TURNOS ---
    // ==========================================
    // avancarTurno() é o ÚNICO lugar que decide quem age a seguir.
    // Ele passa a vez para o próximo herói vivo; quando todos os heróis já
    // agiram na rodada, ele mesmo aciona o turno dos monstros e devolve o
    // log dessas ações para quem chamou (a GUI só exibe o texto, não decide
    // quando os monstros atacam).
    private String avancarTurno() {
        if (verificarDerrota()) return "";

        indiceHeroiAtual++;

        // Pula o turno de heróis que já não estão vivos
        while (indiceHeroiAtual < herois.size() && !herois.get(indiceHeroiAtual).estaVivo()) {
            indiceHeroiAtual++;
        }

        String logMonstros = "";

        if (indiceHeroiAtual >= herois.size()) {
            // A rodada de heróis terminou: agora é a vez dos monstros
            logMonstros = turnoDosMonstros();

            if (!verificarDerrota()) {
                indiceHeroiAtual = 0;
                while (indiceHeroiAtual < herois.size() && !herois.get(indiceHeroiAtual).estaVivo()) {
                    indiceHeroiAtual++;
                }
            }
        }

        return logMonstros;
    }

    private String turnoDosMonstros() {
        StringBuilder log = new StringBuilder();

        for (Monstro monstro : monstros) {
            if (!monstro.estaVivo()) continue;

            Heroi alvoSorteado = sortearHeroiVivo();

            if (alvoSorteado != null) {
                ResultadoAcao acao = monstro.atacar(alvoSorteado);
                log.append(acao.getMensagem()).append("\n");
            }
        }

        if (verificarDerrota()) {
            this.combateAtivo = false;
        }

        return log.toString();
    }

    private Heroi sortearHeroiVivo() {
        List<Heroi> vivos = new ArrayList<>();
        for (Heroi h : herois) {
            if (h.estaVivo()) {
                vivos.add(h);
            }
        }

        if (vivos.isEmpty()) {
            return null;
        }

        int indiceAleatorio = sorteador.nextInt(vivos.size());
        return vivos.get(indiceAleatorio);
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

        if (!resultado.isSucesso()) {
            return resultado;
        }

        StringBuilder mensagem = new StringBuilder(resultado.getMensagem());

        if (verificarVitoria()) {
            this.combateAtivo = false;
        } else {
            // avancarTurno() decide sozinho se é a vez do próximo herói
            // ou se a rodada virou e os monstros devem atacar agora.
            String logMonstros = avancarTurno();
            if (!logMonstros.isEmpty()) {
                mensagem.append("\n").append(logMonstros);
            }
        }

        return new ResultadoAcao(true, mensagem.toString());
    }

    public ResultadoAcao processarAcaoHeroiItem(int indiceHeroi, int indiceItem) {
        if (!isCombateAtivo()) return new ResultadoAcao(false, "A batalha já acabou.");

        Heroi consumidor = this.herois.get(indiceHeroi);
        ResultadoAcao resultado = consumidor.usarItem(indiceItem);

        if (!resultado.isSucesso()) {
            return resultado;
        }

        StringBuilder mensagem = new StringBuilder(resultado.getMensagem());

        if (verificarVitoria()) {
            this.combateAtivo = false;
        } else {
            String logMonstros = avancarTurno();
            if (!logMonstros.isEmpty()) {
                mensagem.append("\n").append(logMonstros);
            }
        }

        return new ResultadoAcao(true, mensagem.toString());
    }
}
