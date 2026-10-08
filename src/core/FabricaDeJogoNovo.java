package core;

import entidades.Goblin;
import entidades.Grupo;
import entidades.Guerreiro;
import entidades.Heroi;
import entidades.Mago;
import entidades.Monstro;
import equipamentos.CatalogoDeEquipamentos;
import itens.CatalogoDeItens;
import itens.Item;
import java.util.ArrayList;
import java.util.List;
import loja.CatalogoDeLojas;
import mundo.CarregadorMapa;
import mundo.EstadoMasmorra;
import mundo.Masmorra;
import objetos.Bau;
import objetos.Mercador;

/**
 * Sabe montar o estado inicial de uma partida nova: quais heróis existem,
 * com que atributos, o que cada um carrega na mochila, o que já vem
 * equipado, e qual masmorra o jogo começa carregando. Fica aqui, e não em
 * JanelaPrincipal, porque isso é regra de jogo (balanceamento, composição
 * do grupo inicial, mapa inicial) -- não é responsabilidade de uma janela
 * Swing decidir isso, só de exibir.
 */
public class FabricaDeJogoNovo {

    public static Grupo criarGrupoInicial() {
        Heroi arthur = new Guerreiro("Arthur", 100, 15, 5, 50);
        Heroi merlin = new Mago("Mago Merlin", 80, 5, 2, 120);
        // (Nome, Vida, PoderBase, Defesa, Mana)

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

    // O conteúdo da masmorra 1 (antes criado dentro da própria Masmorra).
    public static Masmorra criarMasmorraInicial() {
        // Encontro de teste que já existia no protótipo original.
        List<Monstro> grupoTeste = new ArrayList<>();
        grupoTeste.add(new Goblin("Goblin 1"));
        grupoTeste.add(new Goblin("Goblin 2"));
        grupoTeste.add(new Goblin("Goblin 3"));
        List<Masmorra.Encontro> encontros = new ArrayList<>();
        encontros.add(new Masmorra.Encontro(10, 5, grupoTeste));

        List<Bau> baus = new ArrayList<>();

        List<Item> itensDoBauUm = new ArrayList<>();
        itensDoBauUm.add(CatalogoDeItens.pocaoDeMana());
        itensDoBauUm.add(CatalogoDeItens.pocaoDeVida());
        baus.add(new Bau(17, 6, itensDoBauUm, List.of(CatalogoDeEquipamentos.acessorioDoViajante())));

        List<Item> itensDoBauDois = new ArrayList<>();
        itensDoBauDois.add(CatalogoDeItens.pocaoDeVida());
        baus.add(new Bau(10, 10, itensDoBauDois, List.of(CatalogoDeEquipamentos.cajadoAncestral())));

        // Mercador perto do ponto de partida, longe do encontro e dos baús.
        // O que ele vende é definido no CatalogoDeLojas, não aqui.
        List<Mercador> mercadores = new ArrayList<>();
        mercadores.add(new Mercador(5, 12, CatalogoDeLojas.lojaDaMasmorra()));

        EstadoMasmorra estado = new EstadoMasmorra(5, 5, baus, encontros, mercadores);
        return new Masmorra(CarregadorMapa.carregarDeTexto("masmorra1.txt"), estado);
    }
}
