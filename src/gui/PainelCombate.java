package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import core.Combate;
import entidades.Heroi;
import entidades.Personagem;
import habilidades.ResultadoAcao;

public class PainelCombate extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Combate combate;
    private Heroi heroi;
    private Personagem inimigo;
    
    // Elementos da HUD
    private JProgressBar barraHPHeroi;
    private JTextArea logBatalha;
    private JButton btnAtaque;
    private JButton btnMagia;
    private JButton btnItem;
    
    // Cores clássicas de RPG
    private final Color AZUL_RPG = new Color(0, 0, 128);
    private final Color BRANCO = Color.WHITE;
    
    public PainelCombate(Combate combate) {
        this.combate = combate;
        this.heroi = combate.getHeroi();
        this.inimigo = combate.getInimigo();

        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK); // Fundo geral preto

        // ==========================================
        // 1. ÁREA DA ARENA (Centro)
        // ==========================================
        JPanel painelArena = new JPanel();
        painelArena.setBackground(Color.DARK_GRAY); 
        // Aqui futuramente você vai desenhar as imagens dos personagens
        JLabel lblPlaceholder = new JLabel("ÁREA DE COMBATE (Sprites no futuro)");
        lblPlaceholder.setForeground(BRANCO);
        painelArena.add(lblPlaceholder);
        this.add(painelArena, BorderLayout.CENTER);

        // ==========================================
        // 2. MENU CLÁSSICO DE RPG (Sul)
        // ==========================================
        JPanel painelHUD = new JPanel(new GridLayout(1, 3, 5, 0));
        painelHUD.setBackground(Color.BLACK);
        painelHUD.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        painelHUD.setPreferredSize(new Dimension(800, 180)); // Altura fixa para o menu inferior

        // --- CAIXA 1: Log da Batalha (Esquerda) ---
        JPanel painelLog = criarPainelAzul();
        painelLog.setLayout(new BorderLayout());
        logBatalha = new JTextArea();
        logBatalha.setBackground(AZUL_RPG);
        logBatalha.setForeground(BRANCO);
        logBatalha.setEditable(false);
        logBatalha.setLineWrap(true);
        logBatalha.setFont(new Font("Monospaced", Font.BOLD, 12));
        logBatalha.append("Um " + inimigo.getNome() + " apareceu!\n");
        painelLog.add(new JScrollPane(logBatalha), BorderLayout.CENTER);
        
        // --- CAIXA 2: Comandos (Centro) ---
        JPanel painelComandos = criarPainelAzul();
        painelComandos.setLayout(new GridLayout(3, 1, 0, 10)); // 3 linhas (ações)
        
        btnAtaque = estilizarBotao("Ataque");
        btnMagia = estilizarBotao("Magia");
        btnItem = estilizarBotao("Item");
        
        painelComandos.add(btnAtaque);
        painelComandos.add(btnMagia);
        painelComandos.add(btnItem);

        // --- CAIXA 3: Status da Party (Direita) ---
        JPanel painelStatus = criarPainelAzul();
        // GridLayout(4,1) prepara o terreno para até 4 personagens na equipe!
        painelStatus.setLayout(new GridLayout(4, 1)); 
        
        // Linha do Herói atual
        JPanel linhaHeroi = new JPanel(new GridLayout(1, 2));
        linhaHeroi.setBackground(AZUL_RPG);
        JLabel lblNomeHeroi = new JLabel(heroi.getNome());
        lblNomeHeroi.setForeground(BRANCO);
        lblNomeHeroi.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        barraHPHeroi = new JProgressBar(0, heroi.getVida().getMaxima());
        barraHPHeroi.setForeground(new Color(50, 205, 50)); // Verde clássico
        barraHPHeroi.setBackground(Color.DARK_GRAY);
        barraHPHeroi.setStringPainted(true);
        
        linhaHeroi.add(lblNomeHeroi);
        linhaHeroi.add(barraHPHeroi);
        painelStatus.add(linhaHeroi);
        
        // Montando o HUD final
        painelHUD.add(painelLog);
        painelHUD.add(painelComandos);
        painelHUD.add(painelStatus);
        
        this.add(painelHUD, BorderLayout.SOUTH);

        // ==========================================
        // 3. EVENTOS DOS BOTÕES
        // ==========================================
        btnAtaque.addActionListener(e -> {
            ResultadoAcao resultado = combate.processarAcaoHeroiHabilidade(0);
            logBatalha.append("\n" + resultado.getMensagem());
            if (resultado.isSucesso() && combate.batalhaAtiva()) processarTurnoInimigo();
            verificarFimDeJogo();
        });

        btnMagia.addActionListener(e -> {
            ResultadoAcao resultado = combate.processarAcaoHeroiHabilidade(1);
            logBatalha.append("\n" + resultado.getMensagem());
            if (resultado.isSucesso() && combate.batalhaAtiva()) processarTurnoInimigo();
            verificarFimDeJogo();
        });

        btnItem.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Número do slot (ex: 0):");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    ResultadoAcao resultado = combate.processarAcaoHeroiItem(Integer.parseInt(input));
                    logBatalha.append("\n" + resultado.getMensagem());
                    if (resultado.isSucesso() && combate.batalhaAtiva()) processarTurnoInimigo();
                    verificarFimDeJogo();
                } catch (NumberFormatException ex) {
                    logBatalha.append("\nEntrada inválida!");
                }
            }
        });

        atualizarStatus();
    }
    
    // ---------------------------------------------------
    // MÉTODOS DE DESIGN E LÓGICA
    // ---------------------------------------------------
    
    // Cria aquele fundo azul escuro com borda grossa branca
    private JPanel criarPainelAzul() {
        JPanel painel = new JPanel();
        painel.setBackground(AZUL_RPG);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BRANCO, 3), // Borda externa grossa
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Margem interna
        ));
        return painel;
    }
    
    // Remove o visual de "botão de Windows" e deixa como texto de menu
    private JButton estilizarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(AZUL_RPG);
        btn.setForeground(BRANCO);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(JButton.LEFT); // Alinha o texto à esquerda igual no FF
        return btn;
    }

    private void atualizarStatus() {
        barraHPHeroi.setValue(heroi.getVida().getAtual());
        barraHPHeroi.setString(heroi.getVida().getAtual() + "/" + heroi.getVida().getMaxima());
    }

    private void processarTurnoInimigo() {
        ResultadoAcao resultado = combate.processarTurnoInimigo();
        logBatalha.append("\n> " + resultado.getMensagem());
    }

    private void verificarFimDeJogo() {
        atualizarStatus();
        if (!combate.batalhaAtiva()) {
            logBatalha.append("\n\n=== " + combate.verificarVencedor() + " ===");
            btnAtaque.setEnabled(false);
            btnMagia.setEnabled(false);
            btnItem.setEnabled(false);
        }
    }
}