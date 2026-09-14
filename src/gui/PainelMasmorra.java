package gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList; // Novas importações
import java.util.List;
import entidades.Monstro;
import entidades.Goblin;
import entidades.Esqueleto;

public class PainelMasmorra extends JPanel implements KeyListener {
    
    private JanelaPrincipal janela;
    
    private int heroiX = 5; 
    private int heroiY = 5;
    
    // 1. Coordenadas do Inimigo Invisível
    private int goblinX = 10; 
    private int goblinY = 5;
    
    // 2. Flag de controle
    private boolean goblinDerrotado = false; 
    
    private final int TAMANHO_CELULA = 50;

    public PainelMasmorra(JanelaPrincipal janela) {
        this.janela = janela;
        setBackground(Color.DARK_GRAY);
        setFocusable(true); 
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Pinta o herói (Azul)
        g.setColor(Color.BLUE);
        g.fillRect(heroiX * TAMANHO_CELULA, heroiY * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);
        
        // Pinta o Goblin (Vermelho) apenas se ele ainda estiver vivo
        if (!goblinDerrotado) {
            g.setColor(Color.RED);
            g.fillRect(goblinX * TAMANHO_CELULA, goblinY * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();
        
        if (tecla == KeyEvent.VK_W || tecla == KeyEvent.VK_UP) heroiY--;
        if (tecla == KeyEvent.VK_S || tecla == KeyEvent.VK_DOWN) heroiY++;
        if (tecla == KeyEvent.VK_A || tecla == KeyEvent.VK_LEFT) heroiX--;
        if (tecla == KeyEvent.VK_D || tecla == KeyEvent.VK_RIGHT) heroiX++;
        
        // 3. Sistema de Colisão (O Gatilho)
        if (heroiX == goblinX && heroiY == goblinY && !goblinDerrotado) {
            
            // Instancia a lista temporária exigida pela JanelaPrincipal
            List<Monstro> inimigos = new ArrayList<>();
            inimigos.add(new Goblin("Vitor Santos"));
            inimigos.add(new Esqueleto("Vitor Santos 2"));
            
            // Marca como derrotado para não lutar novamente ao pisar aqui
            goblinDerrotado = true;
            
            // Dispara a troca de telas!
            janela.iniciarCombate(inimigos);
        }
        
        repaint(); 
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}