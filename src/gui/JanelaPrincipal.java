package gui;

import javax.swing.JFrame;
import core.Combate; 
import entidades.Personagem;
import entidades.Esqueleto;
import entidades.Heroi;

public class JanelaPrincipal extends JFrame {
    
    public JanelaPrincipal() {
        this.setTitle("Projeto Poo RPG");
        this.setSize(800, 600);             
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        this.setLocationRelativeTo(null);
        
        Heroi heroi = new Heroi("Arthur", 100, 18, 5, 30);
        Personagem esqueleto = new Esqueleto("Esqueleto Safado");

        // 1. Instanciamos o Controlador da batalha
        Combate combate = new Combate(heroi, esqueleto);

        // 2. Passamos o controlador para a View (Painel)
        PainelCombate painelCombate = new PainelCombate(combate);
        this.setContentPane(painelCombate);
        
        this.setVisible(true); 
    }
}