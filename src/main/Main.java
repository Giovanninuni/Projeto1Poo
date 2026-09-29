package main;

import javax.swing.SwingUtilities;
import gui.JanelaPrincipal;

public class Main {
    public static void main(String[] args) {
        
        // O SwingUtilities.invokeLater é a forma mais segura e profissional 
        // de iniciar uma interface gráfica em Java, evitando travamentos na tela.
        SwingUtilities.invokeLater(() -> {
            
            // Instancia a janela principal que construímos e a torna visível.
            new JanelaPrincipal().setVisible(true);
            
        });
        
    }
}