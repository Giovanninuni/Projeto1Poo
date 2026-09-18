package gui;

import entidades.Heroi;
import entidades.Guerreiro;
import entidades.Mago;
import entidades.Grupo;
import core.Combate;
import equipamentos.CatalogoDeEquipamentos;
import itens.PocaoMana;
import itens.PocaoVida;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;
import mundo.CarregadorMapa;
import mundo.Masmorra;

public class JanelaPrincipal extends JFrame {
    
    private CardLayout gerenciadorTelas;
    private JPanel painelTelas;
    
    private Grupo grupo;
    private Masmorra masmorra;
    private PainelMasmorra telaMasmorra;
    private Masmorra.Encontro encontroAtual;

    public JanelaPrincipal() {
        super("RPG - A Vingança contra Vitor S.");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 2. Criando a Party
        Heroi arthur = new Guerreiro("Arthur", 100, 15, 5, 50);
        Heroi merlin = new Mago("Mago Merlin", 80, 5, 2, 120);

        // Cada herói começa com poções básicas na mochila
        arthur.getInventario().adicionarItem(new PocaoVida("Poção de Vida", "Restaura 30 de vida", 30));
        arthur.getInventario().adicionarItem(new PocaoVida("Poção de Vida", "Restaura 30 de vida", 30));
        arthur.getInventario().adicionarItem(new PocaoMana("Poção de Mana", "Restaura 20 de mana", 20));

        merlin.getInventario().adicionarItem(new PocaoVida("Poção de Vida", "Restaura 30 de vida", 30));
        merlin.getInventario().adicionarItem(new PocaoMana("Poção de Mana", "Restaura 20 de mana", 20));
        merlin.getInventario().adicionarItem(new PocaoMana("Poção de Mana", "Restaura 20 de mana", 20));

        List<Heroi> listaHerois = new ArrayList<>();
        listaHerois.add(arthur);
        listaHerois.add(merlin); // Segundo membro!
        this.grupo = new Grupo(listaHerois);

        // Equipamentos iniciais de cada heroi (ver CatalogoDeEquipamentos)
        arthur.equipar(CatalogoDeEquipamentos.espadaDeTreino(), grupo.getDeposito());
        arthur.equipar(CatalogoDeEquipamentos.armaduraDeCouro(), grupo.getDeposito());

        merlin.equipar(CatalogoDeEquipamentos.cajadoDeAprendiz(), grupo.getDeposito());
        merlin.equipar(CatalogoDeEquipamentos.vestesDeAprendiz(), grupo.getDeposito());

        this.masmorra = new Masmorra(CarregadorMapa.carregarDeTmx("mapa1.tmx"));

        this.gerenciadorTelas = new CardLayout();
        this.painelTelas = new JPanel(this.gerenciadorTelas);
        setContentPane(painelTelas);

        this.telaMasmorra = new PainelMasmorra(this, this.masmorra);
        this.painelTelas.add(telaMasmorra, "TELA_MASMORRA");
        this.gerenciadorTelas.show(painelTelas, "TELA_MASMORRA");
        telaMasmorra.requestFocusInWindow();
    }

    // Recebe o encontro inteiro (não só a lista de monstros) para poder
    // marcá-lo como derrotado depois, quando a batalha for de fato vencida.
    public void iniciarCombate(Masmorra.Encontro encontro) {
        this.encontroAtual = encontro;

        // A classe Combate cruza a lista de heróis com a lista de inimigos
        Combate combate = new Combate(this.grupo.getHerois(), encontro.getInimigos());
        PainelCombate telaCombate = new PainelCombate(this, combate);

        painelTelas.add(telaCombate, "TELA_COMBATE");
        gerenciadorTelas.show(painelTelas, "TELA_COMBATE");
    }

    // Chamado só quando o Combate confirma vitória (o botão "Continuar" só
    // existe nesse caso) — é aqui, e não no instante de pisar no tile, que
    // o encontro é marcado como derrotado de verdade.
    public void concluirVitoria() {
        if (encontroAtual != null) {
            encontroAtual.marcarDerrotado();
            encontroAtual = null;
        }
        voltarMasmorra();
    }

    public void voltarMasmorra() {
        gerenciadorTelas.show(painelTelas, "TELA_MASMORRA");
        telaMasmorra.requestFocusInWindow();
    }

    public Grupo getGrupo() {
        return this.grupo;
    }
}