package gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import core.Combate; 
import entidades.Personagem;
import entidades.Esqueleto;
import entidades.Heroi;

public class JanelaPrincipal extends JFrame {
    
    public JanelaPrincipal() {
    	
        this.setTitle("Projeto Poo RPG");
        this.setSize(1024, 768);   // 16 x 12 tiles  
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        this.setLocationRelativeTo(null);
        
        Heroi arthur = new Heroi("Arthur", 100, 18, 5, 30);
        Personagem esqueleto = new Esqueleto("Esqueleto Safado");

        // Empacota os personagens em listas
        List<Heroi> grupoHerois = new ArrayList<>();
        grupoHerois.add(arthur);

        List<Personagem> grupoInimigos = new ArrayList<>();
        grupoInimigos.add(esqueleto);

        // Envia os grupos para o Combate
        Combate combate = new Combate(grupoHerois, grupoInimigos);
        PainelCombate painelCombate = new PainelCombate(combate);
        this.setContentPane(painelCombate);
        
        this.setVisible(true); 
    }
}