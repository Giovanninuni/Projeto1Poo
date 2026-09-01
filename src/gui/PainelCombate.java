package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import entidades.Heroi;
import entidades.Personagem;

public class PainelCombate extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Heroi heroi;
    private Personagem inimigo;
    
    private JLabel lblStatusHeroi;
    private JLabel lblStatusInimigo;
    private JTextArea logBatalha;
    private JButton btnAtaque;
    private JButton btnMagia;
    private JButton btnItem;
    
    public PainelCombate(Heroi heroi, Personagem inimigo) {
        this.heroi = heroi;
        this.inimigo = inimigo;

        // Define o layout principal deste painel como BorderLayout
        this.setLayout(new BorderLayout());

       
        lblStatusHeroi = new JLabel(heroi.getNome() + " - HP: " + heroi.getVida() + " | MP: " + heroi.getMana());
        lblStatusInimigo = new JLabel(inimigo.getNome() + " - HP: " + inimigo.getVida());
        
        JPanel painelSuperior = new JPanel(new GridLayout(1, 2));
        painelSuperior.add(lblStatusHeroi);
        painelSuperior.add(lblStatusInimigo);
        this.add(painelSuperior, BorderLayout.NORTH);
        
        logBatalha = new JTextArea();
        logBatalha.setEditable(false);
        logBatalha.append("=== A BATALHA COMEÇOU ===\n");
        JScrollPane scroll = new JScrollPane(logBatalha);
        this.add(scroll, BorderLayout.CENTER);
        
        btnAtaque = new JButton("Golpe de Espada");
        btnMagia = new JButton("Bola de Fogo (7 MP)");
        btnItem = new JButton("Abrir Mochila");
        
        JPanel painelBotoes = new JPanel(new GridLayout(1, 3));
        painelBotoes.add(btnAtaque);
        painelBotoes.add(btnMagia);
        painelBotoes.add(btnItem);
        this.add(painelBotoes, BorderLayout.SOUTH);
        
     // Botão 1: Golpe de Espada
        btnAtaque.addActionListener(e -> {
            logBatalha.append("\n" + heroi.getNome() + " desferiu um Golpe de Espada!\n");
            heroi.atacar(inimigo, 1);
            atualizarStatus();
            processarTurnoInimigo();
        });

        // Botão 2: Bola de Fogo
        btnMagia.addActionListener(e -> {
            if (heroi.getMana() >= 7) {
                logBatalha.append("\n" + heroi.getNome() + " conjurou Bola de Fogo!\n");
                heroi.atacar(inimigo, 2);
                atualizarStatus();
                processarTurnoInimigo();
            } else {
                logBatalha.append("\nMana insuficiente para conjurar Bola de Fogo!\n");
            }
        });
        
    }
    
    private void atualizarStatus() {
    	    lblStatusHeroi.setText(heroi.getNome() + " - HP: " + heroi.getVida() + "/" + heroi.getVidaMaxima() + " | MP: " + heroi.getMana() + "/" + heroi.getManaMaxima());
    	    lblStatusInimigo.setText(inimigo.getNome() + " - HP: " + inimigo.getVida() + "/" + inimigo.getVidaMaxima());
    }
    
    private void processarTurnoInimigo() {
        if (inimigo.estaVivo()) {
            logBatalha.append("\n--- Turno de " + inimigo.getNome() + " ---\n");
            inimigo.atacar(heroi);
            logBatalha.append(inimigo.getNome() + " atacou você!\n");
            atualizarStatus();

            if (!heroi.estaVivo()) {
                logBatalha.append("\n=== VOCÊ FOI DERROTADO! ===\n");
                desabilitarBotoes();
            }
        } else {
            logBatalha.append("\n=== VITÓRIA! Você derrotou " + inimigo.getNome() + "! ===\n");
            desabilitarBotoes();
        }
    }

    private void desabilitarBotoes() {
        btnAtaque.setEnabled(false);
        btnMagia.setEnabled(false);
        btnItem.setEnabled(false);
    }
}