package equipamentos;

import atributos.Atributos;
import java.util.EnumMap;
import java.util.Map;

/**
 * Os 4 slots de equipamento de um heroi (um Equipamento por TipoEquipamento).
 * Equipar aplica o bonus de ataque/defesa direto nos Atributos do heroi;
 * trocar o equipamento de um slot remove o bonus antigo antes de aplicar o novo.
 */
public class Equipagem {
    private final Map<TipoEquipamento, Equipamento> slots = new EnumMap<>(TipoEquipamento.class);

    public Equipamento equipar(Atributos atributos, Equipamento novo) {
        Equipamento antigo = slots.get(novo.getTipo());

        if (antigo != null) {
            atributos.reduzirAtaque(antigo.getBonusAtaque());
            atributos.reduzirDefesa(antigo.getBonusDefesa());
        }

        atributos.aumentarAtaque(novo.getBonusAtaque());
        atributos.aumentarDefesa(novo.getBonusDefesa());
        slots.put(novo.getTipo(), novo);

        return antigo;
    }

    // Esvazia um slot sem colocar outro equipamento no lugar (diferente de
    // equipar, que so troca). Retorna o que estava equipado ali, ou null
    // se o slot ja estava vazio.
    public Equipamento desequipar(Atributos atributos, TipoEquipamento tipo) {
        Equipamento atual = slots.get(tipo);

        if (atual != null) {
            atributos.reduzirAtaque(atual.getBonusAtaque());
            atributos.reduzirDefesa(atual.getBonusDefesa());
            slots.remove(tipo);
        }

        return atual;
    }

    public Equipamento getArma() {
        return slots.get(TipoEquipamento.ARMA);
    }

    public Equipamento getElmo() {
        return slots.get(TipoEquipamento.ELMO);
    }

    public Equipamento getArmadura() {
        return slots.get(TipoEquipamento.ARMADURA);
    }

    public Equipamento getAcessorio() {
        return slots.get(TipoEquipamento.ACESSORIO);
    }
}
