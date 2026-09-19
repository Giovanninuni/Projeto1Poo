package core;

import entidades.Grupo;
import entidades.Guerreiro;
import entidades.Heroi;
import entidades.Mago;
import equipamentos.CatalogoDeEquipamentos;
import itens.CatalogoDeItens;
import java.util.List;

/**
 * Sabe montar o estado inicial de uma partida nova: quais heróis existem,
 * com que atributos, o que cada um carrega na mochila e o que já vem
 * equipado. Fica aqui, e não em JanelaPrincipal, porque isso é regra de
 * jogo (balanceamento, composição do grupo inicial) -- não é
 * responsabilidade de uma janela Swing decidir isso, só de exibir.
 */
public class FabricaDeJogoNovo {

    public static Grupo criarGrupoInicial() {
        Heroi arthur = new Guerreiro("Arthur", 100, 15, 5, 50);
        Heroi merlin = new Mago("Mago Merlin", 80, 5, 2, 120);

        // Cada herói começa com poções básicas na mochila
        arthur.getInventario().adicionarItem(CatalogoDeItens.pocaoDeVida());
        arthur.getInventario().adicionarItem(CatalogoDeItens.pocaoDeVida());
        arthur.getInventario().adicionarItem(CatalogoDeItens.pocaoDeMana());

        merlin.getInventario().adicionarItem(CatalogoDeItens.pocaoDeVida());
        merlin.getInventario().adicionarItem(CatalogoDeItens.pocaoDeMana());
        merlin.getInventario().adicionarItem(CatalogoDeItens.pocaoDeMana());

        Grupo grupo = new Grupo(List.of(arthur, merlin));

        // Equipamentos iniciais de cada heroi (ver CatalogoDeEquipamentos)
        arthur.equipar(CatalogoDeEquipamentos.espadaDeTreino(), grupo.getDeposito());
        arthur.equipar(CatalogoDeEquipamentos.armaduraDeCouro(), grupo.getDeposito());

        merlin.equipar(CatalogoDeEquipamentos.cajadoDeAprendiz(), grupo.getDeposito());
        merlin.equipar(CatalogoDeEquipamentos.vestesDeAprendiz(), grupo.getDeposito());

        return grupo;
    }
}
