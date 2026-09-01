package core;

import javax.swing.JFrame;
import javax.swing.JButton;

public class Tela extends JFrame {
	public Tela() {
	setTitle("Minha Janela");
	setSize(700, 500); 			
	setResizable(false);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	setLocationRelativeTo(null);
	
	
	setVisible(true); 
}
}