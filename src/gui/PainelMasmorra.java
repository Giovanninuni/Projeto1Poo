package gui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import entidades.Monstro;
import mundo.Masmorra;

public class PainelMasmorra extends JPanel implements KeyListener {

    private JanelaPrincipal janela;
    private Masmorra masmorra;

    private final int TAMANHO_CELULA = 50;

    public PainelMasmorra(JanelaPrincipal janela) {
        this.janela = janela;
        this.masmorra = new Masmorra(20, 15);
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Pinta o herói (Azul) na posição informada pelo modelo
        g.setColor(Color.BLUE);
        g.fillRect(masmorra.getHeroiX() * TAMANHO_CELULA, masmorra.getHeroiY() * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);

        // Pinta cada encontro (Vermelho) que ainda não foi derrotado
        g.setColor(Color.RED);
        for (Masmorra.Encontro encontro : masmorra.getEncontros()) {
            if (!encontro.isDerrotado()) {
                g.fillRect(encontro.getX() * TAMANHO_CELULA, encontro.getY() * TAMANHO_CELULA, TAMANHO_CELULA, TAMANHO_CELULA);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();

        int dx = 0;
        int dy = 0;
        if (tecla == KeyEvent.VK_W || tecla == KeyEvent.VK_UP) dy = -1;
        if (tecla == KeyEvent.VK_S || tecla == KeyEvent.VK_DOWN) dy = 1;
        if (tecla == KeyEvent.VK_A || tecla == KeyEvent.VK_LEFT) dx = -1;
        if (tecla == KeyEvent.VK_D || tecla == KeyEvent.VK_RIGHT) dx = 1;

        if (dx != 0 || dy != 0) {
            masmorra.mover(dx, dy);
        }

        // Sistema de Colisão: pergunta ao modelo se há um encontro aqui
        Masmorra.Encontro encontro = masmorra.getEncontroNaPosicaoDoHeroi();
        if (encontro != null) {
            encontro.marcarDerrotado();

            List<Monstro> inimigos = encontro.getInimigos();

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
