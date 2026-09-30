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

    // Map.put já devolve o que estava no slot antes (ou null se estava vazio)
    public Equipamento equipar(Atributos atributos, Equipamento novo) {
        Equipamento antigo = slots.put(novo.getTipo(), novo);

        if (antigo != null) {
            removerBonus(atributos, antigo);
        }
        aplicarBonus(atributos, novo);

        return antigo;
    }

    // Esvazia um slot sem colocar outro equipamento no lugar (diferente de
    // equipar, que so troca). Retorna o que estava equipado ali, ou null
    // se o slot ja estava vazio. Map.remove também devolve o valor removido.
    public Equipamento desequipar(Atributos atributos, TipoEquipamento tipo) {
        Equipamento atual = slots.remove(tipo);

        if (atual != null) {
            removerBonus(atributos, atual);
        }

        return atual;
    }

    private void aplicarBonus(Atributos atributos, Equipamento equipamento) {
        atributos.aumentarPoder(equipamento.getBonusPoder());
        atributos.aumentarDefesa(equipamento.getBonusDefesa());
    }

    private void removerBonus(Atributos atributos, Equipamento equipamento) {
        atributos.reduzirPoder(equipamento.getBonusPoder());
        atributos.reduzirDefesa(equipamento.getBonusDefesa());
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
