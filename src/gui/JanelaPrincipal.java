package gui;

import entidades.Heroi;
import entidades.Guerreiro;
import entidades.Mago;
import entidades.Monstro;
import core.Combate;
import itens.PocaoMana;
import itens.PocaoVida;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

public class JanelaPrincipal extends JFrame {
    
    private CardLayout gerenciadorTelas;
    private JPanel painelTelas;
    
    //Agora temos um ESQUADRÃO persistente
    private List<Heroi> grupoHerois;
    private PainelMasmorra telaMasmorra;

    public JanelaPrincipal() {
        super("RPG - A Vingança contra Vitor S.");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 2. Criando a Party estilo Final Fantasy
        Heroi arthur = new Guerreiro("Arthur", 100, 15, 5, 50);
        Heroi merlin = new Mago("Mago Merlin", 80, 5, 2, 120);

        // Cada herói começa com poções básicas na mochila
        arthur.getInventario().adicionarItem(new PocaoVida("Poção de Vida", "Restaura 30 de vida", 30));
        arthur.getInventario().adicionarItem(new PocaoVida("Poção de Vida", "Restaura 30 de vida", 30));
        arthur.getInventario().adicionarItem(new PocaoMana("Poção de Mana", "Restaura 20 de mana", 20));

        merlin.getInventario().adicionarItem(new PocaoVida("Poção de Vida", "Restaura 30 de vida", 30));
        merlin.getInventario().adicionarItem(new PocaoMana("Poção de Mana", "Restaura 20 de mana", 20));
        merlin.getInventario().adicionarItem(new PocaoMana("Poção de Mana", "Restaura 20 de mana", 20));

        this.grupoHerois = new ArrayList<>();
        this.grupoHerois.add(arthur);
        this.grupoHerois.add(merlin); // Segundo membro!

        this.gerenciadorTelas = new CardLayout();
        this.painelTelas = new JPanel(this.gerenciadorTelas);
        setContentPane(painelTelas);
        
        this.telaMasmorra = new PainelMasmorra(this);
        this.painelTelas.add(telaMasmorra, "TELA_MASMORRA");
        this.gerenciadorTelas.show(painelTelas, "TELA_MASMORRA");
        telaMasmorra.requestFocusInWindow();
    }

    //O método recebe a gangue de inimigos inteira enviada pelo mapa
    public void iniciarCombate(List<Monstro> grupoInimigos) {
        // A classe Combate cruza a lista de heróis com a lista de inimigos
        Combate combate = new Combate(this.grupoHerois, grupoInimigos);
        PainelCombate telaCombate = new PainelCombate(this, combate);
        
        painelTelas.add(telaCombate, "TELA_COMBATE");
        gerenciadorTelas.show(painelTelas, "TELA_COMBATE");
    }

    public void voltarMasmorra() {
        gerenciadorTelas.show(painelTelas, "TELA_MASMORRA");
        telaMasmorra.requestFocusInWindow();
    }
    
    public List<Heroi> getHerois(){
        return this.grupoHerois;
    }
}