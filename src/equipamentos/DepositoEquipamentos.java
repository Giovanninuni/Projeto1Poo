package equipamentos;

import java.util.ArrayList;
import java.util.List;

/**
 * Guarda equipamentos achados (ex: em baus) que ainda nao foram equipados
 * em nenhum heroi. E compartilhado pelo grupo todo (fica em Grupo), assim
 * como o Ouro -- diferente do Inventario, que e por heroi.
 */
public class DepositoEquipamentos {
    private List<Equipamento> equipamentos = new ArrayList<>();

    public void adicionar(Equipamento equipamento) {
        equipamentos.add(equipamento);
    }

    public Equipamento retirar(int indiceUsuario) {
        int indiceReal = indiceUsuario - 1;

        if (indiceReal >= 0 && indiceReal < equipamentos.size()) {
            return equipamentos.remove(indiceReal);
        }

        return null;
    }

    public String[] obterMenu() {
        String[] menu = new String[equipamentos.size()];

        for (int i = 0; i < equipamentos.size(); i++) {
            menu[i] = (i + 1) + " - " + equipamentos.get(i).getNome();
        }

        return menu;
    }

    public boolean estaVazio() {
        return equipamentos.isEmpty();
    }
}
