package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import core.Combate;
import entidades.Heroi;
import entidades.Personagem;
import habilidades.ResultadoAcao;

public class PainelCombate extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Combate combate; // O Controlador entra em cena
    private Heroi heroi;
    private Personagem inimigo;
    
    private JLabel lblStatusHeroi;
    private JLabel lblStatusInimigo;
    private JTextArea logBatalha;
    private JButton btnAtaque;
    private JButton btnMagia;
    private JButton btnItem;
    
    public PainelCombate(Combate combate) {
        this.combate = combate;
        this.heroi = combate.getHeroi();
        this.inimigo = combate.getInimigo();

        this.setLayout(new BorderLayout());

        // Inicializa os status e chama o método para preencher os textos
        lblStatusHeroi = new JLabel();
        lblStatusInimigo = new JLabel();
        atualizarStatus();
        
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
        
        // Botão 1: Golpe de Espada (Índice 0 na lista de habilidades)
        btnAtaque.addActionListener(e -> {
            ResultadoAcao resultado = combate.processarAcaoHeroiHabilidade(0);
            logBatalha.append("\n" + resultado.getMensagem() + "\n");
            
            // Se o herói agiu com sucesso, é a vez do inimigo
            if (resultado.isSucesso() && combate.batalhaAtiva()) {
                processarTurnoInimigo();
            }
            verificarFimDeJogo();
        });

        // Botão 2: Bola de Fogo (Índice 1 na lista de habilidades)
        btnMagia.addActionListener(e -> {
            // Repare como não checamos a mana aqui! O backend decide e devolve o texto.
            ResultadoAcao resultado = combate.processarAcaoHeroiHabilidade(1);
            logBatalha.append("\n" + resultado.getMensagem() + "\n");
            
            if (resultado.isSucesso() && combate.batalhaAtiva()) {
                processarTurnoInimigo();
            }
            verificarFimDeJogo();
        });
        
        // Botão 3: Abrir Mochila
        btnItem.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Digite o número do espaço na mochila (ex: 0):");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    int indice = Integer.parseInt(input);
                    ResultadoAcao resultado = combate.processarAcaoHeroiItem(indice);
                    logBatalha.append("\n" + resultado.getMensagem() + "\n");
                    
                    if (resultado.isSucesso() && combate.batalhaAtiva()) {
                        processarTurnoInimigo();
                    }
                    verificarFimDeJogo();
                } catch (NumberFormatException ex) {
                    logBatalha.append("\nEntrada inválida! Digite um número.\n");
                }
            }
        });
    }
    
    private void atualizarStatus() {
        // Como 'getVida()' e 'getMana()' já retornam objetos (Value Objects) que sabem se imprimir, 
        // nós apenas concatenamos. Isso evita erros de chamar métodos antigos como getVidaMaxima().
        lblStatusHeroi.setText(heroi.getNome() + " - HP: " + heroi.getVida() + " | MP: " + heroi.getMana());
        lblStatusInimigo.setText(inimigo.getNome() + " - HP: " + inimigo.getVida());
    }
    
    private void processarTurnoInimigo() {
        logBatalha.append("--- Turno de " + inimigo.getNome() + " ---\n");
        ResultadoAcao resultado = combate.processarTurnoInimigo();
        logBatalha.append(resultado.getMensagem() + "\n");
    }

    private void verificarFimDeJogo() {
        atualizarStatus();
        if (!combate.batalhaAtiva()) {
            logBatalha.append("\n=== " + combate.verificarVencedor() + " ===\n");
            desabilitarBotoes();
        }
    }

    private void desabilitarBotoes() {
        btnAtaque.setEnabled(false);
        btnMagia.setEnabled(false);
        btnItem.setEnabled(false);
    }
}