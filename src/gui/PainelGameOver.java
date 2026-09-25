package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
        // mesmo estilo do botão Voltar do PainelStatus
        btnRecomecar.addActionListener(e -> janela.reiniciarJogo());

        JPanel rodape = new JPanel();
        rodape.setBackground(Color.BLACK);
        rodape.add(btnRecomecar);
        this.add(rodape, BorderLayout.SOUTH);
    }
}