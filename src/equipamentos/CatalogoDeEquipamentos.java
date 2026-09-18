package equipamentos;

import entidades.Guerreiro;
import entidades.Mago;

/**
 * Ponto unico de criacao dos equipamentos do jogo. Em vez de montar um
 * "new Equipamento(...)" toda vez que um bau (ou futuramente uma loja, um
 * drop de monstro etc.) precisa dar um equipamento, quem precisa pede um
 * metodo daqui -- assim a definicao de cada equipamento (nome, bonus,
 * restricao de classe) fica num lugar so.
 *
 * Cada metodo retorna uma instancia NOVA a cada chamada, de proposito: se
 * dois baus dessem o mesmo objeto Equipamento, eles estariam sem querer
 * compartilhando o mesmo item.
 */
public class CatalogoDeEquipamentos {

    public static Equipamento acessorioDoViajante() {
        return new Equipamento("Acessório do Viajante", "Aumenta o ataque em 2", TipoEquipamento.ACESSORIO, 2, 0);
    }

    public static Equipamento cajadoAncestral() {
        return new Equipamento("Cajado Ancestral", "Cajado mágico ancestral, só pode ser usado por magos", TipoEquipamento.ARMA, 5, 0, Mago.class);
    }

    // Equipamentos iniciais de cada heroi (ver JanelaPrincipal).
    public static Equipamento espadaDeTreino() {
        return new Equipamento("Espada de Treino", "Espada básica de treino, +3 de ataque", TipoEquipamento.ARMA, 3, 0, Guerreiro.class);
    }

    public static Equipamento armaduraDeCouro() {
        return new Equipamento("Armadura de Couro", "Armadura leve, +2 de defesa", TipoEquipamento.ARMADURA, 0, 2);
    }

    public static Equipamento cajadoDeAprendiz() {
        return new Equipamento("Cajado de Aprendiz", "Cajado básico de estudo, +3 de ataque", TipoEquipamento.ARMA, 3, 0, Mago.class);
    }

    public static Equipamento vestesDeAprendiz() {
        return new Equipamento("Vestes de Aprendiz", "Robe leve, +1 de defesa", TipoEquipamento.ARMADURA, 0, 1);
    }
}
