package core;

import entidades.Esqueleto;
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
import objetos.Porta;

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

        // Ouro inicial: dá pra comprar algo na loja antes da primeira batalha
        grupo.getOuro().adicionar(150);

        // Equipamentos iniciais de cada heroi (ver CatalogoDeEquipamentos)
        arthur.equipar(CatalogoDeEquipamentos.espadaDeTreino(), grupo.getDeposito());
        arthur.equipar(CatalogoDeEquipamentos.armaduraDeCouro(), grupo.getDeposito());

        merlin.equipar(CatalogoDeEquipamentos.cajadoDeAprendiz(), grupo.getDeposito());
        merlin.equipar(CatalogoDeEquipamentos.vestesDeAprendiz(), grupo.getDeposito());

        return grupo;
    }

    /**
     * O conteúdo da masmorra 1 (assets/mapas/masmorra1.txt). O caminho
     * pensado é uma progressão de dificuldade, com recompensa depois de
     * cada luta:
     *
     *   1. Sala de entrada (sudoeste): o herói começa aqui, ao lado do
     *      mercador, para comprar algo antes da primeira luta.
     *   2. Corredor estreito: os goblins bloqueiam a passagem para a sala
     *      leste (não dá pra desviar, o corredor tem 1 tile de altura).
     *   3. Sala leste: baú com a recompensa dos goblins e a porta na parede
     *      de cima.
     *   4. Atrás da porta, os esqueletos (grupo mais forte) guardam a saída
     *      da passagem. Depois deles, o salão norte, com o baú do cajado
     *      no canto mais distante.
     */
    public static Masmorra criarMasmorraInicial() {
        List<Masmorra.Encontro> encontros = new ArrayList<>();

        // Primeiro grupo: goblins no gargalo do corredor entre as salas.
        List<Monstro> goblins = new ArrayList<>();
        goblins.add(new Goblin("Goblin 1"));
        goblins.add(new Goblin("Goblin 2"));
        goblins.add(new Goblin("Goblin 3"));
        encontros.add(new Masmorra.Encontro(12, 17, goblins));

        // Segundo grupo, mais forte: esqueletos logo na saída da porta.
        // O mapa mostra o primeiro monstro da lista (um esqueleto).
        List<Monstro> esqueletos = new ArrayList<>();
        esqueletos.add(new Esqueleto("Esqueleto 1"));
        esqueletos.add(new Esqueleto("Esqueleto 2"));
        encontros.add(new Masmorra.Encontro(22, 8, esqueletos));

        List<Bau> baus = new ArrayList<>();

        // Recompensa dos goblins, no canto da sala leste.
        List<Item> itensDoBauUm = new ArrayList<>();
        itensDoBauUm.add(CatalogoDeItens.pocaoDeMana());
        itensDoBauUm.add(CatalogoDeItens.pocaoDeVida());
        baus.add(new Bau(27, 13, itensDoBauUm, List.of(CatalogoDeEquipamentos.acessorioDoViajante())));

        // Recompensa final, no canto do salão norte (ao lado dos ossos).
        List<Item> itensDoBauDois = new ArrayList<>();
        itensDoBauDois.add(CatalogoDeItens.pocaoDeVida());
        baus.add(new Bau(3, 3, itensDoBauDois, List.of(CatalogoDeEquipamentos.cajadoAncestral())));

        // A porta fica na linha de baixo da parede da sala leste; a passagem
        // atravessa a parede grossa até o salão norte (linha 8).
        List<Porta> portas = new ArrayList<>();
        portas.add(new Porta(22, 12, false));

        // Mercador na sala de entrada, perto do ponto de partida.
        // O que ele vende é definido no CatalogoDeLojas, não aqui.
        List<Mercador> mercadores = new ArrayList<>();
        mercadores.add(new Mercador(2, 14, CatalogoDeLojas.lojaDaMasmorra()));

        EstadoMasmorra estado = new EstadoMasmorra(4, 16, baus, encontros, mercadores, portas);
        return new Masmorra(CarregadorMapa.carregarDeTexto("masmorra1.txt"), estado);
    }
}
