package gui;

import entidades.Grupo;
import core.Combate;
import core.EstadoBatalha;
import core.FabricaDeJogoNovo;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import mundo.Masmorra;

public class JanelaPrincipal extends JFrame {
    
    private CardLayout gerenciadorTelas;
    private JPanel painelTelas;
    private JPanel telaTemporaria; // combate, status ou game over que está por cima da masmorra
    
    private Grupo grupo;
    private Masmorra masmorra;
    private PainelMasmorra telaMasmorra;
    private Masmorra.Encontro encontroAtual;

    public JanelaPrincipal() {
    	super("RPG - A Vingança contra Vitor S.");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.gerenciadorTelas = new CardLayout();
        this.painelTelas = new JPanel(this.gerenciadorTelas);
        setContentPane(painelTelas);

        iniciarPartida();
    }
    
 // Monta uma partida do zero: usado ao abrir o jogo e ao recomeçar após o Game Over.
    private void iniciarPartida() {
        this.grupo = FabricaDeJogoNovo.criarGrupoInicial();
        this.masmorra = FabricaDeJogoNovo.criarMasmorraInicial();

        this.telaMasmorra = new PainelMasmorra(this, this.masmorra);
        painelTelas.add(telaMasmorra, "TELA_MASMORRA");
        voltarMasmorra();
    }

    public void reiniciarJogo() {
        painelTelas.remove(telaMasmorra); // tira o painel da partida antiga
        iniciarPartida();
    }
    
	 // Mostra uma tela temporária e descarta a anterior. A masmorra nunca é
	 // removida aqui: é a tela permanente para onde o jogo sempre volta.
    private void mostrarTelaTemporaria(JPanel novaTela, String nome) {
    	painelTelas.add(novaTela, nome);
	    gerenciadorTelas.show(painelTelas, nome);
	    if (telaTemporaria != null) {
	        painelTelas.remove(telaTemporaria);
	    }
	    telaTemporaria = novaTela;
	}

    // Recebe o encontro inteiro (não só a lista de monstros) para poder
    // marcá-lo como derrotado depois, quando a batalha for de fato vencida.
    public void iniciarCombate(Masmorra.Encontro encontro) {
        this.encontroAtual = encontro;

        // A classe Combate cruza a lista de heróis com a lista de inimigos
        Combate combate = new Combate(this.grupo.getHerois(), encontro.getInimigos(), this.grupo.getOuro());
        PainelCombate telaCombate = new PainelCombate(this, combate);
        
        mostrarTelaTemporaria(telaCombate, "TELA_COMBATE");
    }

    // Único ponto de saída do combate: a GUI só informa o resultado, aqui se
    // decide para onde ir. O encontro só é marcado como derrotado aqui, na
    // vitória confirmada — não no instante em que o herói pisa no tile.
    public void concluirCombate(EstadoBatalha resultado) {
    	switch (resultado) {
    	//com a seta executa normal, mas ele para no primeiro case que entra, não precisando de break
    		case VITORIA -> {
    			encontroAtual.marcarDerrotado();
    			voltarMasmorra();
    		}
    
    		case FUGA -> {
    			voltarMasmorra();
    		}
    			
    		case DERROTA -> {
    			mostrarGameOver();
    		}
    		
    		case EM_ANDAMENTO -> {}
    	}
    	// Se nao tem nenhum, nao esta em batalha
    	encontroAtual = null;
    }

    public void voltarMasmorra() {
        gerenciadorTelas.show(painelTelas, "TELA_MASMORRA");
        telaMasmorra.requestFocusInWindow();
        if (telaTemporaria != null) {
            painelTelas.remove(telaTemporaria);
            telaTemporaria = null;
        }
    }

    // Recria a tela de status a cada chamada pra sempre refletir o estado
    // atual do grupo (mesmo padrão do iniciarCombate).
    public void mostrarStatus() {
        PainelStatus telaStatus = new PainelStatus(this, this.grupo);
        mostrarTelaTemporaria(telaStatus, "TELA_STATUS");
    }
    
    public void mostrarGameOver() {
        PainelGameOver telaGameOver = new PainelGameOver(this);
        mostrarTelaTemporaria(telaGameOver, "TELA_GAME_OVER");
    }

    public Grupo getGrupo() {
        return this.grupo;
    }
}