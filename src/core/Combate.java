package core;

import entidades.Heroi;
import entidades.Monstro;
import entidades.Personagem;
import ataques.Ataque;
import ataques.Habilidade;
import acoes.ResultadoAcao;
import atributos.Dano;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Combate {

    private List<Heroi> herois;
    private List<Monstro> monstros;

    private int indiceHeroiAtual = 0;
    private boolean combateAtivo = true;
    private int ouroGanho = 0;
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
                ResultadoAcao acao = executarAtaque(monstro, alvoSorteado, monstro.getAtaque());
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

        List<Habilidade> habilidadesHeroi = atacante.getHabilidades();
        if (indiceHabilidade < 0 || indiceHabilidade >= habilidadesHeroi.size()) {
            return new ResultadoAcao(false, "Habilidade inválida ou não encontrada!");
        }
        Habilidade habilidade = habilidadesHeroi.get(indiceHabilidade);

        if (!atacante.getMana().gastar(habilidade.getCustoMana())) {
            return new ResultadoAcao(false, String.format(
                "%s não tem mana suficiente para usar %s!", atacante.getNome(), habilidade.getNome()));
        }

        ResultadoAcao resultado = executarAtaque(atacante, alvo, habilidade);

        return finalizarAcao(new StringBuilder(resultado.getMensagem()));
    }

    public ResultadoAcao processarAtaqueBasicoHeroi(int indiceHeroi, int indiceAlvo) {
        if (!isCombateAtivo()) return new ResultadoAcao(false, "A batalha já acabou.");

        Heroi atacante = this.herois.get(indiceHeroi);
        Monstro alvo = this.monstros.get(indiceAlvo);

        if (!alvo.estaVivo()) {
            return new ResultadoAcao(false, "O alvo já está derrotado!");
        }

        ResultadoAcao resultado = executarAtaque(atacante, alvo, atacante.getAtaquePadrao());

        return finalizarAcao(new StringBuilder(resultado.getMensagem()));
    }

    public ResultadoAcao processarAcaoHeroiItem(int indiceHeroi, int indiceItem) {
        if (!isCombateAtivo()) return new ResultadoAcao(false, "A batalha já acabou.");

        Heroi consumidor = this.herois.get(indiceHeroi);
        ResultadoAcao resultado = consumidor.usarItem(indiceItem);

        if (!resultado.isSucesso()) {
            return resultado;
        }

        return finalizarAcao(new StringBuilder(resultado.getMensagem()));
    }

    // avancarTurno() decide sozinho se é a vez do próximo herói ou se a
    // rodada virou e os monstros devem atacar agora; se a vitória aconteceu
    // nesta ação, concede as recompensas em vez de continuar os turnos.
    private ResultadoAcao finalizarAcao(StringBuilder mensagem) {
        if (verificarVitoria()) {
            this.combateAtivo = false;
            mensagem.append("\n").append(concederRecompensas());
        } else {
            String logMonstros = avancarTurno();
            if (!logMonstros.isEmpty()) {
                mensagem.append("\n").append(logMonstros);
            }
        }

        return new ResultadoAcao(true, mensagem.toString());
    }

    // ==========================================
    // --- RECOMPENSAS DE VITÓRIA ---
    // ==========================================
    // XP é dividido entre os heróis vivos; ouro vai pro contador único do
    // grupo (quem lê getOuroGanho() e credita é a JanelaPrincipal).
    private String concederRecompensas() {
        int xpTotal = 0;
        int ouroTotal = 0;
        for (Monstro m : monstros) {
            xpTotal += m.getXpConcedida();
            ouroTotal += m.getOuroDropado();
        }

        List<Heroi> vivos = new ArrayList<>();
        for (Heroi h : herois) {
            if (h.estaVivo()) {
                vivos.add(h);
            }
        }

        int xpPorHeroi = vivos.isEmpty() ? 0 : xpTotal / vivos.size();

        StringBuilder mensagem = new StringBuilder(
            String.format("O grupo ganhou %d de ouro! Cada sobrevivente recebeu %d de XP.", ouroTotal, xpPorHeroi));

        for (Heroi h : vivos) {
            int nivelAntes = h.getNivel();
            h.ganharXp(xpPorHeroi);
            if (h.getNivel() > nivelAntes) {
                mensagem.append(String.format("%n%s subiu para o nível %d!", h.getNome(), h.getNivel()));
            }
        }

        this.ouroGanho = ouroTotal;

        return mensagem.toString();
    }

    public int getOuroGanho() {
        return ouroGanho;
    }

    // ==========================================
    // --- APLICAÇÃO DE DANO ---
    // ==========================================
    // Único ponto do sistema que resolve um ataque: o Ataque só calcula o
    // Dano (calcularDano), quem aplica no alvo e monta a mensagem é o
    // Combate — evita repetir esse padrão em cada Habilidade/Monstro.
    private ResultadoAcao executarAtaque(Personagem atacante, Personagem alvo, Ataque ataque) {
        Dano dano = ataque.calcularDano(atacante);
        int danoSofrido = alvo.receberDano(dano);

        String mensagem;
        if (dano.isCritico()) {
            mensagem = String.format("ACERTO CRÍTICO! %s usa %s em %s, causando %d de dano!",
                atacante.getNome(), ataque.getNome(), alvo.getNome(), danoSofrido);
        } else {
            mensagem = String.format("%s usa %s em %s, causando %d de dano.",
                atacante.getNome(), ataque.getNome(), alvo.getNome(), danoSofrido);
        }

        return new ResultadoAcao(true, mensagem);
    }
}
