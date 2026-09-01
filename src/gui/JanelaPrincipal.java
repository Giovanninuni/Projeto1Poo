package gui;

import javax.swing.JFrame;

import entidades.Personagem;
import entidades.Goblin;
import entidades.Heroi;

import javax.swing.JButton;

public class JanelaPrincipal extends JFrame {
	public JanelaPrincipal() {
	this.setTitle("Projeto Poo RPG");
	this.setSize(800, 600); 			
	this.setResizable(false);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	this.setLocationRelativeTo(null);
	
	Heroi heroi = new Heroi("Arthur", 100, 18, 5, 30);
	Personagem goblin = new Goblin("Goblin Ladrão");

	PainelCombate painelCombate = new PainelCombate(heroi, goblin);
	this.setContentPane(painelCombate); // Define que este painel preencherá a janela
	
	this.setVisible(true); 
}
}
