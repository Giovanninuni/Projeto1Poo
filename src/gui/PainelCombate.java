package gui;

import entidades.Heroi;
import entidades.Personagem;
import itens.Item;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;

public class PainelCombate extends JPanel {
    private static final long serialVersionUID = 1L;

    private JanelaPrincipal janela;
    private Heroi heroi;
    private Personagem inimigo;

    // Componentes visuais
    private JLabel lblHeroiStatus;
    private JLabel lblInimigoStatus;
    private JTextArea logBatalha;
    private JButton btnAtacar;
    private JButton btnMagia;
    private JButton btnItem;

    public PainelCombate(JanelaPrincipal janela, Heroi heroi, Personagem inimigo) {
        this.janela = janela;
        this.heroi = heroi;
        this.inimigo = inimigo;

        setLayout(new BorderLayout());

        // 1. Topo: Informações de Vida e Mana (JLabels)
        JPanel painelStatus = new JPanel(new GridLayout(1, 2));
        lblHeroiStatus = new JLabel(heroi.getNome() + " | HP: " + heroi.getVida() + " | MP: " + heroi.getMana());
        lblInimigoStatus = new JLabel(inimigo.getNome() + " | HP: " + inimigo.getVida());
        painelStatus.add(lblHeroiStatus);
        painelStatus.add(lblInimigoStatus);
        add(painelStatus, BorderLayout.NORTH);

        // 2. Centro: Log de texto dos acontecimentos
        logBatalha = new JTextArea();
        logBatalha.setEditable(false);
        add(new JScrollPane(logBatalha), BorderLayout.CENTER);

        // 3. Base: Botões de Ação
        JPanel painelBotoes = new JPanel(new GridLayout(1, 3));
        btnAtacar = new JButton("Golpe de Espada");
        btnMagia = new JButton("Bola de Fogo (7 MP)");
        btnItem = new JButton("Usar Poção");

        painelBotoes.add(btnAtacar);
        painelBotoes.add(btnMagia);
        painelBotoes.add(btnItem);
        add(painelBotoes, BorderLayout.SOUTH);

        // Configura os cliques dos botões
        configurarEventos();
    }

    private void configurarEventos() {
        btnAtacar.addActionListener(e -> {
            heroi.atacar(inimigo);
            atualizarTela();
            turnoInimigo();
        });

        // Configuração dos outros botões...
    }

    private void atualizarTela() {
        lblHeroiStatus.setText(heroi.getNome() + " | HP: " + heroi.getVida() + " | MP: " + heroi.getMana());
        lblInimigoStatus.setText(inimigo.getNome() + " | HP: " + inimigo.getVida());
    }

    private void turnoInimigo() {
        if (inimigo.estaVivo()) {
            inimigo.atacar(heroi);
            atualizarTela();
        }
    }
}