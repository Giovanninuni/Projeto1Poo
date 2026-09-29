package core;

import entidades.Heroi;
import entidades.Monstro;
import entidades.Personagem;
import ataques.Ataque;
import ataques.Habilidade;
import atributos.Dano;
import atributos.Ouro;
import contratos.ResultadoAcao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Combate {

    private List<Heroi> herois;
    private List<Monstro> monstros;
    private Ouro ouro;
    private EstadoBatalha estadoBatalha;

    private int indiceHeroiAtual = 0;
    private final Random sorteador = new Random();

    private static final String MSG_BATALHA_ACABOU = "A batalha já acabou.";

    // Recebe o Ouro do grupo (não o Grupo inteiro) pelo mesmo motivo de só
    // receber List<Heroi> em vez de Grupo: Combate só deve enxergar o que
    // precisa pra creditar recompensa, não o depósito de equipamentos nem
    // o resto do estado do grupo.
    public Combate(List<Heroi> grupoHerois, List<Monstro> grupoMonstros, Ouro ouro) {
        this.herois = grupoHerois;
        this.monstros = grupoMonstros;
        this.ouro = ouro;
        this.estadoBatalha = EstadoBatalha.EM_ANDAMENTO;
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
        if (batalhaEncerrada()) {
            return "";
        }

        indiceHeroiAtual++;
        pularHeroisMortos();

        String logMonstros = "";

        if (indiceHeroiAtual >= herois.size()) {
            // A rodada de heróis terminou: agora é a vez dos monstros
            logMonstros = turnoDosMonstros();

            if (estadoBatalha == EstadoBatalha.EM_ANDAMENTO) {
                indiceHeroiAtual = 0;
                pularHeroisMortos();
            }
        }

        return logMonstros;
    }

    // Avança o índice até o próximo herói vivo (ou até o fim da lista)
    private void pularHeroisMortos() {
        while (indiceHeroiAtual < herois.size() && !herois.get(indiceHeroiAtual).estaVivo()) {
            indiceHeroiAtual++;
        }
    }

    private List<Heroi> heroisVivos() {
        List<Heroi> vivos = new ArrayList<>();
        for (Heroi h : herois) {
            if (h.estaVivo()) {
                vivos.add(h);
            }
        }
        return vivos;
    }

    private String turnoDosMonstros() {
        StringBuilder log = new StringBuilder();

        for (Monstro monstro : monstros) {
            if (!monstro.estaVivo()) continue;

            Heroi alvoSorteado = sortearHeroiVivo();

            if (alvoSorteado != null) {
                log.append(executarAtaque(monstro, alvoSorteado, monstro.getAtaque())).append("\n");
            }
        }

        atualizarEstado();
        
        return log.toString();
    }

    private Heroi sortearHeroiVivo() {
        List<Heroi> vivos = heroisVivos();

        if (vivos.isEmpty()) {
            return null;
        }

        int indiceAleatorio = sorteador.nextInt(vivos.size());
        return vivos.get(indiceAleatorio);
    }

    private boolean verificarVitoria() {
        for (Monstro m : monstros) {
            if (m.estaVivo())  return false;
        }
        return true;
    }

    private boolean verificarDerrota() {
        return heroisVivos().isEmpty();
    }

    private void atualizarEstado() {
        if (verificarVitoria()) {
            estadoBatalha = EstadoBatalha.VITORIA;
        } else if (verificarDerrota()) {
            estadoBatalha = EstadoBatalha.DERROTA;
        }
    }

    private boolean batalhaEncerrada() {
        return estadoBatalha != EstadoBatalha.EM_ANDAMENTO;
    }

    public List<Heroi> getHerois() {
        return herois;
    }

    public List<Monstro> getMonstros() {
        return monstros;
    }

    public Heroi getHeroiAtual() {
        // Impede que o sistema procure um índice inválido quando a party morre
        if (indiceHeroiAtual >= herois.size()) return null;
        return herois.get(indiceHeroiAtual);
    }
    
    public EstadoBatalha getEstado() {
    	return this.estadoBatalha;
    }

    // As ações do herói não recebem "qual herói" de fora: o Combate já sabe
    // de quem é a vez (indiceHeroiAtual). Assim a GUI não precisa descobrir
    // o índice do turno, e não consegue mandar agir um herói fora da vez.
    // A checagem de estado vem antes de getHeroiAtual() de propósito: com a
    // batalha em andamento sempre existe um herói da vez (nunca null).
    public ResultadoAcao processarAcaoHeroiHabilidade(int indiceHabilidade, int indiceAlvo) {
        if (batalhaEncerrada()) {
            return new ResultadoAcao(false, MSG_BATALHA_ACABOU);
        }

        Heroi atacante = getHeroiAtual();
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

        return finalizarAcao(executarAtaque(atacante, alvo, habilidade));
    }

    public ResultadoAcao processarAtaqueBasicoHeroi(int indiceAlvo) {
        if (batalhaEncerrada()) {
            return new ResultadoAcao(false, MSG_BATALHA_ACABOU);
        }

        Heroi atacante = getHeroiAtual();
        Monstro alvo = this.monstros.get(indiceAlvo);

        if (!alvo.estaVivo()) {
            return new ResultadoAcao(false, "O alvo já está derrotado!");
        }

        return finalizarAcao(executarAtaque(atacante, alvo, atacante.getAtaquePadrao()));
    }

    public ResultadoAcao processarAcaoHeroiItem(int indiceItem) {
        if (batalhaEncerrada()) {
            return new ResultadoAcao(false, MSG_BATALHA_ACABOU);
        }

        Heroi consumidor = getHeroiAtual();
        ResultadoAcao resultado = consumidor.usarItem(indiceItem);

        if (!resultado.isSucesso()) {
            return resultado;
        }

        return finalizarAcao(resultado.getMensagem());
    }

    // avancarTurno() decide sozinho se é a vez do próximo herói ou se a
    // rodada virou e os monstros devem atacar agora; se a vitória aconteceu
    // nesta ação, concede as recompensas em vez de continuar os turnos.
    private ResultadoAcao finalizarAcao(String mensagemAcao) {
        StringBuilder mensagem = new StringBuilder(mensagemAcao);
    	atualizarEstado();
        if (estadoBatalha == EstadoBatalha.VITORIA) {
            mensagem.append("\n").append(concederRecompensas());
        } else {
            String logMonstros = avancarTurno();
            if (!logMonstros.isEmpty()) {
                mensagem.append("\n").append(logMonstros);
            }
        }

        return new ResultadoAcao(true, mensagem.toString());
    }

    public ResultadoAcao fugir() {
    	if (batalhaEncerrada()) {
    		return new ResultadoAcao(false, MSG_BATALHA_ACABOU);
    	}
    	
    	boolean conseguiuFugir = sorteador.nextInt(100) < 50;
    	// 50% de chance de fugir
    	
    	if(conseguiuFugir) {
    		this.estadoBatalha = EstadoBatalha.FUGA;
        	return new ResultadoAcao(true, "A equipe fugiu!");
    	}
    	
    	return finalizarAcao("A equipe não conseguiu fugir!");
    	
    } 
    
    
    // ==========================================
    // --- RECOMPENSAS DE VITÓRIA ---
    // ==========================================
    // XP é dividido entre os heróis vivos; ouro é creditado direto no Ouro
    // do grupo, do mesmo jeito que XP é creditado direto em cada Heroi —
    // Combate aplica a própria recompensa, não devolve um número pra
    // outra camada aplicar.
    private String concederRecompensas() {
        int xpTotal = 0;
        int ouroTotal = 0;
        for (Monstro m : monstros) {
            xpTotal += m.getXpConcedida();
            ouroTotal += m.getOuroDropado();
        }

        List<Heroi> vivos = heroisVivos();

        int xpPorHeroi = vivos.isEmpty() ? 0 : xpTotal / vivos.size();

        StringBuilder mensagem = new StringBuilder(
            String.format("O grupo ganhou %d de ouro! Cada sobrevivente recebeu %d de XP.", ouroTotal, xpPorHeroi));

        for (Heroi h : vivos) {
            if (h.ganharXp(xpPorHeroi) > 0) {
                mensagem.append(String.format("%n%s subiu para o nível %d!", h.getNome(), h.getNivel()));
            }
        }

        this.ouro.adicionar(ouroTotal);

        return mensagem.toString();
    }

    // ==========================================
    // --- APLICAÇÃO DE DANO ---
    // ==========================================
    // Único ponto do sistema que resolve um ataque: o Ataque só calcula o
    // Dano (calcularDano), quem aplica no alvo e monta a mensagem é o
    // Combate — evita repetir esse padrão em cada Habilidade/Monstro.
    private String executarAtaque(Personagem atacante, Personagem alvo, Ataque ataque) {
        Dano dano = ataque.calcularDano(atacante);
        int danoSofrido = alvo.receberDano(dano);

        String prefixo = dano.isCritico() ? "ACERTO CRÍTICO! " : "";
        String pontuacao = dano.isCritico() ? "!" : ".";
        return String.format("%s%s usa %s em %s, causando %d de dano%s",
            prefixo, atacante.getNome(), ataque.getNome(), alvo.getNome(), danoSofrido, pontuacao);
    }
}
