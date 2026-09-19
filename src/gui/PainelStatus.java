package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import entidades.Grupo;
import entidades.Heroi;
import equipamentos.Equipagem;
import equipamentos.Equipamento;

/**
 * Tela só de leitura: mostra nível, vida/mana/xp, ataque/defesa totais
 * (já com bonus de equipamento, porque é o mesmo Atributos usado em
 * combate) e os 4 slots de equipamento de cada heroi, alem do ouro do
 * grupo. Mesmo estilo visual de PainelCombate (fundo preto, paineis
 * azuis com borda branca).
 */
public class PainelStatus extends JPanel {
    private static final long serialVersionUID = 1L;

    private final Color AZUL_RPG = new Color(0, 0, 128);
    private final Color BRANCO = Color.WHITE;

    public PainelStatus(JanelaPrincipal janela, Grupo grupo) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        JLabel lblOuro = new JLabel("Ouro do grupo: " + grupo.getOuro().getQuantidade(), JLabel.CENTER);
        lblOuro.setForeground(Color.YELLOW);
        lblOuro.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblOuro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(lblOuro, BorderLayout.NORTH);

        JPanel painelHerois = new JPanel(new GridLayout(1, grupo.getHerois().size(), 10, 0));
        painelHerois.setBackground(Color.BLACK);
        painelHerois.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Heroi heroi : grupo.getHerois()) {
            painelHerois.add(criarCartaoHeroi(heroi));
        }

        this.add(painelHerois, BorderLayout.CENTER);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(AZUL_RPG);
        btnVoltar.setForeground(BRANCO);
        btnVoltar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnVoltar.setFocusPainted(false);
        btnVoltar.addActionListener(e -> janela.voltarMasmorra());

        JPanel painelRodape = new JPanel();
        painelRodape.setBackground(Color.BLACK);
        painelRodape.add(btnVoltar);
        this.add(painelRodape, BorderLayout.SOUTH);
    }

    private JPanel criarCartaoHeroi(Heroi heroi) {
        JPanel cartao = new JPanel();
        cartao.setLayout(new GridLayout(0, 1, 0, 6));
        cartao.setBackground(AZUL_RPG);
        cartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BRANCO, 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblNome = new JLabel(heroi.getNome() + " (" + heroi.getClass().getSimpleName() + ") - Nível " + heroi.getNivel());
        lblNome.setForeground(BRANCO);
        lblNome.setFont(new Font("SansSerif", Font.BOLD, 16));
        cartao.add(lblNome);

        cartao.add(criarBarra("HP", heroi.getVida().getAtual(), heroi.getVida().getMaxima(), new Color(50, 205, 50)));
        cartao.add(criarBarra("MP", heroi.getMana().getAtual(), heroi.getMana().getMaxima(), new Color(30, 144, 255)));
        cartao.add(criarBarra("XP", heroi.getExperiencia().getXpAtual(), heroi.getExperiencia().getXpParaProximoNivel(), Color.ORANGE));

        cartao.add(criarLabel(String.format("Ataque: %d   Defesa: %d",
                heroi.getAtributos().getAtaque(), heroi.getAtributos().getDefesa())));

        cartao.add(criarLabel("— Equipamento —"));
        Equipagem equipagem = heroi.getEquipagem();
        cartao.add(criarLabel("Arma: " + nomeOuVazio(equipagem.getArma())));
        cartao.add(criarLabel("Elmo: " + nomeOuVazio(equipagem.getElmo())));
        cartao.add(criarLabel("Armadura: " + nomeOuVazio(equipagem.getArmadura())));
        cartao.add(criarLabel("Acessório: " + nomeOuVazio(equipagem.getAcessorio())));

        return cartao;
    }

    private String nomeOuVazio(Equipamento equipamento) {
        return equipamento == null ? "— vazio —" : equipamento.getNome();
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(BRANCO);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return label;
    }

    private JProgressBar criarBarra(String prefixo, int atual, int maximo, Color cor) {
        JProgressBar barra = new JProgressBar(0, maximo);
        barra.setValue(atual);
        barra.setForeground(cor);
        barra.setBackground(Color.DARK_GRAY);
        barra.setStringPainted(true);
        barra.setString(prefixo + ": " + atual + "/" + maximo);
        return barra;
    }
}
