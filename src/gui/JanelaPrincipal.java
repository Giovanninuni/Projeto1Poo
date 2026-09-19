package gui;

import entidades.Grupo;
import core.Combate;
import core.FabricaDeJogoNovo;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import mundo.CarregadorMapa;
import mundo.Masmorra;

public class JanelaPrincipal extends JFrame {
    
    private CardLayout gerenciadorTelas;
    private JPanel painelTelas;
    
    private Grupo grupo;
    private Masmorra masmorra;
    private PainelMasmorra telaMasmorra;
    private Masmorra.Encontro encontroAtual;

    public JanelaPrincipal() {
        super("RPG - A Vingança contra Vitor S.");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.grupo = FabricaDeJogoNovo.criarGrupoInicial();
        this.masmorra = new Masmorra(CarregadorMapa.carregarDeTmx("mapa1.tmx"));

        this.gerenciadorTelas = new CardLayout();
        this.painelTelas = new JPanel(this.gerenciadorTelas);
        setContentPane(painelTelas);

        this.telaMasmorra = new PainelMasmorra(this, this.masmorra);
        this.painelTelas.add(telaMasmorra, "TELA_MASMORRA");
        this.gerenciadorTelas.show(painelTelas, "TELA_MASMORRA");
        telaMasmorra.requestFocusInWindow();
    }

    // Recebe o encontro inteiro (não só a lista de monstros) para poder
    // marcá-lo como derrotado depois, quando a batalha for de fato vencida.
    public void iniciarCombate(Masmorra.Encontro encontro) {
        this.encontroAtual = encontro;

        // A classe Combate cruza a lista de heróis com a lista de inimigos
        Combate combate = new Combate(this.grupo.getHerois(), encontro.getInimigos());
        PainelCombate telaCombate = new PainelCombate(this, combate);

        painelTelas.add(telaCombate, "TELA_COMBATE");
        gerenciadorTelas.show(painelTelas, "TELA_COMBATE");
    }

    // Chamado só quando o Combate confirma vitória (o botão "Continuar" só
    // existe nesse caso) — é aqui, e não no instante de pisar no tile, que
    // o encontro é marcado como derrotado de verdade.
    public void concluirVitoria() {
        if (encontroAtual != null) {
            encontroAtual.marcarDerrotado();
            encontroAtual = null;
        }
        voltarMasmorra();
    }

    public void voltarMasmorra() {
        gerenciadorTelas.show(painelTelas, "TELA_MASMORRA");
        telaMasmorra.requestFocusInWindow();
    }

    // Recria a tela de status a cada chamada pra sempre refletir o estado
    // atual do grupo (mesmo padrão do iniciarCombate).
    public void mostrarStatus() {
        PainelStatus telaStatus = new PainelStatus(this, this.grupo);
        painelTelas.add(telaStatus, "TELA_STATUS");
        gerenciadorTelas.show(painelTelas, "TELA_STATUS");
    }

    public Grupo getGrupo() {
        return this.grupo;
    }
}