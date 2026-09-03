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
    private JProgressBar barraMPHeroi;    // NOVA BARRA: Mana do Arthur
    private JProgressBar barraHPInimigo;  // NOVA BARRA: Vida do Inimigo
    
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
        this.setBackground(Color.BLACK); 

        // ==========================================
        // 1. ÁREA DA ARENA (Centro) - ESTILO POKÉMON
        // ==========================================
        JPanel painelArena = new JPanel(new BorderLayout());
        painelArena.setBackground(Color.DARK_GRAY); 
        
        // Criando a HUD flutuante do Inimigo no topo da arena
        JPanel painelInimigoTop = new JPanel();
        painelInimigoTop.setOpaque(false); // Deixa transparente para ver o fundo cinza
        
        JLabel lblNomeInimigo = new JLabel(inimigo.getNome() + "  ");
        lblNomeInimigo.setForeground(BRANCO);
        lblNomeInimigo.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        barraHPInimigo = new JProgressBar(0, inimigo.getVida().getMaxima());
        barraHPInimigo.setForeground(Color.RED);
        barraHPInimigo.setBackground(Color.BLACK);
        barraHPInimigo.setStringPainted(true); // Permite escrever o texto na barra
        barraHPInimigo.setPreferredSize(new Dimension(150, 20));
        
        painelInimigoTop.add(lblNomeInimigo);
        painelInimigoTop.add(barraHPInimigo);
        painelArena.add(painelInimigoTop, BorderLayout.NORTH);

        // Placeholder para o Sprite do monstro
        JLabel lblPlaceholder = new JLabel("SPRITE DO INIMIGO AQUI", JLabel.CENTER);
        lblPlaceholder.setForeground(BRANCO);
        painelArena.add(lblPlaceholder, BorderLayout.CENTER);
        
        this.add(painelArena, BorderLayout.CENTER);

        // ==========================================
        // 2. MENU CLÁSSICO DE RPG (Sul) - ESTILO FF
        // ==========================================
        JPanel painelHUD = new JPanel(new GridLayout(1, 3, 5, 0));
        painelHUD.setBackground(Color.BLACK);
        painelHUD.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        painelHUD.setPreferredSize(new Dimension(800, 180)); 

        // --- CAIXA 1: Log da Batalha (Esquerda) ---
        JPanel painelLog = criarPainelAzul();
        painelLog.setLayout(new BorderLayout());
        logBatalha = new JTextArea();
        logBatalha.setBackground(AZUL_RPG);
        logBatalha.setForeground(BRANCO);
        logBatalha.setEditable(false);
        logBatalha.setLineWrap(true);
        logBatalha.setFont(new Font("Monospaced", Font.BOLD, 12));
        logBatalha.append("Um " + inimigo.getNome() + " selvagem apareceu!\n");
        painelLog.add(new JScrollPane(logBatalha), BorderLayout.CENTER);
        
        // --- CAIXA 2: Comandos (Centro) ---
        JPanel painelComandos = criarPainelAzul();
        // Usando GridLayout(2,2) para deixar no formato de botões do Pokémon!
        painelComandos.setLayout(new GridLayout(2, 2, 5, 5)); 
        
        btnAtaque = estilizarBotao("Ataque");
        btnMagia = estilizarBotao("Magia");
        btnItem = estilizarBotao("Item");
        JButton btnFugir = estilizarBotao("Fugir"); // Botão extra para preencher a grade 2x2
        
        painelComandos.add(btnAtaque);
        painelComandos.add(btnMagia);
        painelComandos.add(btnItem);
        painelComandos.add(btnFugir);

        // --- CAIXA 3: Status da Party (Direita) ---
        JPanel painelStatus = criarPainelAzul();
        painelStatus.setLayout(new GridLayout(4, 1, 0, 5)); // Preparado para 4 heróis
        
        // Linha do Herói atual (Dividida em 3 colunas: Nome, HP, MP)
        JPanel linhaHeroi = new JPanel(new GridLayout(1, 3, 5, 0));
        linhaHeroi.setBackground(AZUL_RPG);
        
        JLabel lblNomeHeroi = new JLabel(heroi.getNome());
        lblNomeHeroi.setForeground(BRANCO);
        lblNomeHeroi.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        barraHPHeroi = new JProgressBar(0, heroi.getVida().getMaxima());
        barraHPHeroi.setForeground(new Color(50, 205, 50)); // Verde
        barraHPHeroi.setBackground(Color.DARK_GRAY);
        barraHPHeroi.setStringPainted(true);
        
        barraMPHeroi = new JProgressBar(0, heroi.getMana().getMaxima());
        barraMPHeroi.setForeground(new Color(30, 144, 255)); // Azul
        barraMPHeroi.setBackground(Color.DARK_GRAY);
        barraMPHeroi.setStringPainted(true);
        
        linhaHeroi.add(lblNomeHeroi);
        linhaHeroi.add(barraHPHeroi);
        linhaHeroi.add(barraMPHeroi);
        
        painelStatus.add(linhaHeroi);
        
        // Adicionando caixas ao HUD principal
        painelHUD.add(painelLog);
        painelHUD.add(painelComandos);
        painelHUD.add(painelStatus);
        
        this.add(painelHUD, BorderLayout.SOUTH);

        // ==========================================
        // 3. EVENTOS DOS BOTÕES
        // ==========================================
        btnAtaque.addActionListener(e -> {
            ResultadoAcao resultado = combate.processarAcaoHeroiHabilidade(0);
            logBatalha.append("\n> " + resultado.getMensagem());
            if (resultado.isSucesso() && combate.batalhaAtiva()) processarTurnoInimigo();
            verificarFimDeJogo();
        });

        btnMagia.addActionListener(e -> {
            ResultadoAcao resultado = combate.processarAcaoHeroiHabilidade(1);
            logBatalha.append("\n> " + resultado.getMensagem());
            if (resultado.isSucesso() && combate.batalhaAtiva()) processarTurnoInimigo();
            verificarFimDeJogo();
        });

        btnItem.addActionListener(e -> {
            // 1. Pede ao inventário a lista de itens formatada
            String[] opcoesMenu = heroi.getInventario().obterMenuDeItens();
            
            // 2. Se o array voltou vazio, nem abre a tela, só avisa no log!
            if (opcoesMenu.length == 0) {
                logBatalha.append("\n> A sua mochila está completamente vazia!");
                return; // Para a execução do botão aqui
            }
            
            // 3. Mostra o pop-up com o Menu Dropdown!
            String escolhido = (String) JOptionPane.showInputDialog(
                    this,
                    "Escolha um item da mochila:",
                    "Abrir Mochila",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoesMenu, // Aqui passamos o array com as opções
                    opcoesMenu[0] // Aqui dizemos qual item vem selecionado por padrão
            );
            
            // 4. Se o jogador escolheu um item e clicou em OK (não cancelou)
            if (escolhido != null) {
                // Truque: O texto escolhido é algo como "0 - Poção". 
                // Vamos quebrar o texto no espaço vazio e pegar só o número "0" da frente!
                int indice = Integer.parseInt(escolhido.split(" ")[0]);
                
                ResultadoAcao resultado = combate.processarAcaoHeroiItem(indice);
                logBatalha.append("\n> " + resultado.getMensagem());
                
                if (resultado.isSucesso() && combate.batalhaAtiva()) {
                    processarTurnoInimigo();
                }
                verificarFimDeJogo();
            }
        });
        
        btnFugir.addActionListener(e -> {
            logBatalha.append("\n> Não há como fugir desta batalha!");
        });

        atualizarStatus(); // Carrega os valores pela primeira vez
    }
    
    // ---------------------------------------------------
    // MÉTODOS DE DESIGN E LÓGICA
    // ---------------------------------------------------
    
    private JPanel criarPainelAzul() {
        JPanel painel = new JPanel();
        painel.setBackground(AZUL_RPG);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BRANCO, 3), 
                BorderFactory.createEmptyBorder(10, 10, 10, 10) 
        ));
        return painel;
    }
    
    private JButton estilizarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(AZUL_RPG);
        btn.setForeground(BRANCO);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(BRANCO, 1)); // Borda fina no botão
        return btn;
    }

    private void atualizarStatus() {
        // Atualiza a barra de HP do Herói
        barraHPHeroi.setValue(heroi.getVida().getAtual());
        barraHPHeroi.setString("HP: " + heroi.getVida().getAtual());
        
        // Atualiza a barra de MP do Herói
        barraMPHeroi.setValue(heroi.getMana().getAtual());
        barraMPHeroi.setString("MP: " + heroi.getMana().getAtual());
        
        // Atualiza a barra de HP do Inimigo
        barraHPInimigo.setValue(inimigo.getVida().getAtual());
        barraHPInimigo.setString(inimigo.getVida().getAtual() + "/" + inimigo.getVida().getMaxima());
    }

    private void processarTurnoInimigo() {
        ResultadoAcao resultado = combate.processarTurnoInimigo();
        logBatalha.append("\n  " + resultado.getMensagem());
    }

    private void verificarFimDeJogo() {
        atualizarStatus(); // Garante que as barras reflitam o golpe final
        if (!combate.batalhaAtiva()) {
            logBatalha.append("\n\n=== " + combate.verificarVencedor() + " ===");
            btnAtaque.setEnabled(false);
            btnMagia.setEnabled(false);
            btnItem.setEnabled(false);
        }
    }
}