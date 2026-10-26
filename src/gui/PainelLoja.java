package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import contratos.Negociavel;
import contratos.ResultadoAcao;
import entidades.Grupo;
import entidades.Heroi;
import equipamentos.Equipamento;
import itens.Item;
import loja.Loja;
import loja.Oferta;

/**
 * Tela da loja: lista as ofertas de itens e de equipamentos com o preço, e
 * um botão de comprar em cada uma. A tela não decide nada: quem cobra o
 * ouro, checa a mochila e entrega a compra é a Loja. Aqui só se mostra o
 * ResultadoAcao que ela devolve. Mesmo estilo visual de PainelStatus.
 */
public class PainelLoja extends JPanel {
    private static final long serialVersionUID = 1L;

    private final Color AZUL_RPG = new Color(0, 0, 128);
    private final Color BRANCO = Color.WHITE;

    private final Loja loja;
    private final Grupo grupo;

    private final JLabel lblOuro;
    private final JLabel lblMensagem;
    private final JComboBox<String> escolhaHeroi;

    public PainelLoja(JanelaPrincipal janela, Loja loja, Grupo grupo) {
        this.loja = loja;
        this.grupo = grupo;

        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK);

        // Topo: nome da loja e ouro do grupo
        JPanel painelTopo = new JPanel(new GridLayout(2, 1));
        painelTopo.setBackground(Color.BLACK);
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JLabel lblNome = new JLabel(loja.getNome(), JLabel.CENTER);
        lblNome.setForeground(BRANCO);
        lblNome.setFont(new Font("SansSerif", Font.BOLD, 24));
        painelTopo.add(lblNome);

        lblOuro = new JLabel("", JLabel.CENTER);
        lblOuro.setForeground(Color.YELLOW);
        lblOuro.setFont(new Font("SansSerif", Font.BOLD, 18));
        painelTopo.add(lblOuro);
        this.add(painelTopo, BorderLayout.NORTH);

        // Centro: itens à esquerda, equipamentos à direita
        JPanel painelOfertas = new JPanel(new GridLayout(1, 2, 10, 0));
        painelOfertas.setBackground(Color.BLACK);
        painelOfertas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        escolhaHeroi = new JComboBox<>(grupo.obterMenuDeHerois());
        painelOfertas.add(criarColunaDeItens());
        painelOfertas.add(criarColunaDeEquipamentos());
        this.add(painelOfertas, BorderLayout.CENTER);

        // Rodapé: resultado da última compra e o botão de voltar
        JPanel painelRodape = new JPanel(new GridLayout(2, 1, 0, 6));
        painelRodape.setBackground(Color.BLACK);
        painelRodape.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        lblMensagem = new JLabel("Bem-vindo! Escolha o que deseja comprar.", JLabel.CENTER);
        lblMensagem.setForeground(BRANCO);
        lblMensagem.setFont(new Font("SansSerif", Font.BOLD, 15));
        painelRodape.add(lblMensagem);

        JButton btnVoltar = criarBotao("Voltar");
        btnVoltar.addActionListener(e -> janela.voltarMasmorra());
        JPanel painelBotao = new JPanel();
        painelBotao.setBackground(Color.BLACK);
        painelBotao.add(btnVoltar);
        painelRodape.add(painelBotao);
        this.add(painelRodape, BorderLayout.SOUTH);

        atualizarOuro();
    }

    private JPanel criarColunaDeItens() {
        JPanel coluna = criarLista("Itens");

        // Item vai para a mochila de um herói, então primeiro escolhe qual
        JPanel linhaHeroi = new JPanel();
        linhaHeroi.setBackground(AZUL_RPG);
        linhaHeroi.add(criarLabel("Para:"));
        linhaHeroi.add(escolhaHeroi);
        coluna.add(linhaHeroi);

        List<Oferta<Item>> ofertas = loja.getOfertasDeItens();
        for (int i = 0; i < ofertas.size(); i++) {
            int indiceMenu = i + 1; // os menus da Loja são 1-based
            coluna.add(criarLinhaDeOferta(ofertas.get(i), () -> {
                Heroi comprador = grupo.getHerois().get(escolhaHeroi.getSelectedIndex());
                return loja.comprarItem(indiceMenu, comprador, grupo.getOuro());
            }));
        }
        return emColuna(coluna);
    }

    private JPanel criarColunaDeEquipamentos() {
        JPanel coluna = criarLista("Equipamentos");
        JLabel lblAviso = criarLabel("Vão para o depósito do grupo (equipe com I)");
        lblAviso.setHorizontalAlignment(JLabel.CENTER);
        coluna.add(lblAviso);

        List<Oferta<Equipamento>> ofertas = loja.getOfertasDeEquipamentos();
        for (int i = 0; i < ofertas.size(); i++) {
            int indiceMenu = i + 1;
            coluna.add(criarLinhaDeOferta(ofertas.get(i),
                    () -> loja.comprarEquipamento(indiceMenu, grupo.getOuro(), grupo.getDeposito())));
        }
        return emColuna(coluna);
    }

    /**
     * Uma linha da vitrine: nome, descrição, preço e o botão de comprar.
     * Recebe um Negociavel, então serve igual para item e para
     * equipamento. A compra em si vem pronta de fora (Compra), porque cada
     * tipo é comprado de um jeito diferente na Loja.
     */
    private JPanel criarLinhaDeOferta(Negociavel oferta, Compra compra) {
        JPanel linha = new JPanel(new BorderLayout(10, 0));
        linha.setBackground(AZUL_RPG);
        linha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BRANCO),
                BorderFactory.createEmptyBorder(6, 4, 6, 4)
        ));

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setBackground(AZUL_RPG);
        JLabel lblNome = criarLabel(oferta.getNome() + "  —  " + loja.precoDeCompra(oferta) + " de ouro");
        lblNome.setFont(new Font("SansSerif", Font.BOLD, 14));
        textos.add(lblNome);
        textos.add(criarLabel(oferta.getDescricao()));
        linha.add(textos, BorderLayout.CENTER);

        JButton btnComprar = criarBotao("Comprar");
        btnComprar.addActionListener(e -> {
            ResultadoAcao resultado = compra.executar();
            lblMensagem.setForeground(resultado.isSucesso() ? new Color(50, 205, 50) : new Color(255, 99, 71));
            lblMensagem.setText(resultado.getMensagem());
            atualizarOuro();
        });
        linha.add(btnComprar, BorderLayout.EAST);
        return linha;
    }

    // Lista de uma coluna (título e linhas, uma embaixo da outra)
    private JPanel criarLista(String titulo) {
        JPanel lista = new JPanel(new GridLayout(0, 1, 0, 4));
        lista.setBackground(AZUL_RPG);

        JLabel lblTitulo = criarLabel(titulo);
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lista.add(lblTitulo);
        return lista;
    }

    // Coluna com borda branca, no estilo dos cartões do PainelStatus. A lista
    // fica no topo (NORTH) pra as linhas não esticarem até o fim da tela.
    private JPanel emColuna(JPanel lista) {
        JPanel coluna = new JPanel(new BorderLayout());
        coluna.setBackground(AZUL_RPG);
        coluna.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BRANCO, 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        coluna.add(lista, BorderLayout.NORTH);
        return coluna;
    }

    private void atualizarOuro() {
        lblOuro.setText("Ouro do grupo: " + grupo.getOuro().getQuantidade());
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setBackground(AZUL_RPG);
        botao.setForeground(BRANCO);
        botao.setFont(new Font("SansSerif", Font.BOLD, 14));
        botao.setFocusPainted(false);
        return botao;
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(BRANCO);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return label;
    }

    // O que o botão "Comprar" de uma linha faz. Item e equipamento chamam
    // métodos diferentes da Loja, então cada linha recebe o seu.
    private interface Compra {
        ResultadoAcao executar();
    }
}
