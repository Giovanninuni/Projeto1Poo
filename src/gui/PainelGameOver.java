package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class PainelGameOver extends JPanel {
    private static final long serialVersionUID = 1L;

    public PainelGameOver(JanelaPrincipal janela) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);
       
        JLabel titulo = new JLabel("GAME OVER", JLabel.CENTER);
        titulo.setForeground(Color.RED);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 64));
        this.add(titulo, BorderLayout.CENTER);

        JButton btnRecomecar = new JButton("Recomeçar");
        estilizarBotaoRecomecar(btnRecomecar);
        btnRecomecar.addActionListener(e -> janela.reiniciarJogo());

        JPanel rodape = new JPanel();
        rodape.setBackground(Color.BLACK);
        rodape.add(btnRecomecar);
        this.add(rodape, BorderLayout.SOUTH);
        rodape.setBorder(BorderFactory.createEmptyBorder(0, 0, 120, 0));
    }
    
    private void estilizarBotaoRecomecar(JButton btnRecomecar) {
    	btnRecomecar.setBackground(new Color(0, 0, 128));
        btnRecomecar.setForeground(Color.WHITE);
        btnRecomecar.setFont(new Font("SansSerif", Font.BOLD, 32));
        btnRecomecar.setFocusPainted(false);
        Border bordaAtual = BorderFactory.createLineBorder(Color.WHITE, 1);
        Border espacamento = BorderFactory.createEmptyBorder(5, 15, 5, 15);
        btnRecomecar.setBorder(BorderFactory.createCompoundBorder(bordaAtual, espacamento));
    }
}