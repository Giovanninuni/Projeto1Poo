package gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Guarda quais teclas estão apertadas AGORA.
 *
 * É o padrão usado em jogos: o KeyListener não move nada, só anota
 * "apertou" e "soltou". Quem move é o game loop (PainelMasmorra), que a
 * cada quadro pergunta ao Teclado o que está apertado naquele instante.
 * Assim o herói para no mesmo quadro em que a tecla é solta, e segurar
 * duas teclas (diagonal) funciona sem esforço.
 *
 * Estende KeyAdapter (e não implementa KeyListener direto) pra não
 * precisar escrever o keyTyped vazio.
 */
public class Teclado extends KeyAdapter {

    private final Set<Integer> apertadas = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        apertadas.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        apertadas.remove(e.getKeyCode());
    }

    /** true se qualquer uma das teclas estiver apertada (ex: W ou seta pra cima). */
    public boolean algumaApertada(int... teclas) {
        for (int tecla : teclas) {
            if (apertadas.contains(tecla)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Esquece todas as teclas. Usado quando o painel perde o foco (diálogo
     * do baú, tela de combate): ele não vai receber o "soltou", e sem isso
     * o herói continuaria andando sozinho.
     */
    public void soltarTodas() {
        apertadas.clear();
    }
}
