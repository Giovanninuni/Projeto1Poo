package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import core.Combate;
import core.EstadoBatalha;
import entidades.Heroi;
import entidades.Monstro;
import ataques.Habilidade;
import contratos.ResultadoAcao;

public class PainelCombate extends JPanel {
	private static final long serialVersionUID = 1L;

	private JanelaPrincipal janela;
	private Combate combate;

	// Variável de controle: -1 = ataque básico (sempre disponível), >=0 = índice na
	// lista de habilidades
	private int habilidadeArmada = -1;

	// Listas para armazenar as barras e nomes da HUD dinâmica
	private List<JProgressBar> barrasHPHerois = new ArrayList<>();
	private List<JProgressBar> barrasMPHerois = new ArrayList<>();
	private List<JProgressBar> barrasHPMonstros = new ArrayList<>();
	private List<JLabel> labelsNomesHerois = new ArrayList<>();

	// Elementos gerais da HUD
	private JTextArea logBatalha;
	private JButton btnAtaque;
	private JButton btnHabilidade;
	private JButton btnItem;
	private JPanel painelComandos;
	private JButton btnContinuar;

	// Cores clássicas de RPG
	private final Color AZUL_RPG = new Color(0, 0, 128);
	private final Color BRANCO = Color.WHITE;

	public PainelCombate(JanelaPrincipal janela, Combate combate) {
		this.janela = janela;
		this.combate = combate;

		this.setLayout(new BorderLayout());
		this.setBackground(Color.BLACK);

		// ==========================================
		// 1. ÁREA DA ARENA (Centro) - LISTA DINÂMICA
		// ==========================================
		JPanel painelArena = new JPanel(new GridLayout(1, combate.getMonstros().size(), 10, 0));
		painelArena.setBackground(Color.DARK_GRAY);
		painelArena.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		for (int i = 0; i < combate.getMonstros().size(); i++) {
			Monstro monstroAtual = combate.getMonstros().get(i);
			final int indiceAlvo = i; // Necessário para o escopo do botão

			JPanel painelMonstro = new JPanel(new BorderLayout());
			painelMonstro.setOpaque(false);

			// Botão que representa o monstro e serve como alvo
			JButton btnAlvo = new JButton(monstroAtual.getNome());
			btnAlvo.setFont(new Font("SansSerif", Font.BOLD, 14));

			// Barra de vida individual
			JProgressBar barraHP = new JProgressBar(0, monstroAtual.getVida().getMaxima());
			barraHP.setValue(monstroAtual.getVida().getAtual());
			barraHP.setForeground(Color.RED);
			barraHP.setBackground(Color.BLACK);
			barraHP.setStringPainted(true);
			barrasHPMonstros.add(barraHP);

			btnAlvo.addActionListener(e -> {
				// Dispara a ação "armada" no alvo clicado. -1 é o ataque
				// básico (sempre disponível); >=0 é o índice na lista de
				// habilidades (as que custam mana). O Combate já decide
				// sozinho, internamente, se depois dessa ação é a vez do
				// próximo herói ou se os monstros atacam agora (o log
				// dessas ações já vem embutido na mensagem de retorno).
				ResultadoAcao resultado = (habilidadeArmada == -1)
						? combate.processarAtaqueBasicoHeroi(indiceAlvo)
						: combate.processarAcaoHeroiHabilidade(habilidadeArmada, indiceAlvo);
				
				aposAcao(resultado);
			});

			painelMonstro.add(barraHP, BorderLayout.NORTH);
			painelMonstro.add(btnAlvo, BorderLayout.CENTER);
			painelArena.add(painelMonstro);
		}

		this.add(painelArena, BorderLayout.CENTER);

		// ==========================================
		// 2. MENU CLÁSSICO DE RPG (Sul) - ESTILO FF
		// ==========================================
		JPanel painelHUD = new JPanel(new GridLayout(1, 3, 5, 0));
		painelHUD.setBackground(Color.BLACK);
		painelHUD.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		painelHUD.setPreferredSize(new Dimension(0, 180));

		// --- CAIXA 1: Log da Batalha (Esquerda) ---
		JPanel painelLog = criarPainelAzul();
		painelLog.setLayout(new BorderLayout());
		logBatalha = new JTextArea();
		logBatalha.setBackground(AZUL_RPG);
		logBatalha.setForeground(BRANCO);
		logBatalha.setEditable(false);
		logBatalha.setLineWrap(true);
		logBatalha.setFont(new Font("Monospaced", Font.BOLD, 12));
		logBatalha.append("Inimigos selvagens apareceram!\n");
		painelLog.add(new JScrollPane(logBatalha), BorderLayout.CENTER);

		// --- CAIXA 2: Comandos (Centro) ---
		painelComandos = criarPainelAzul();
		painelComandos.setLayout(new GridLayout(2, 2, 5, 5));

		btnAtaque = estilizarBotao("Ataque");
		btnHabilidade = estilizarBotao("Habilidade");
		btnItem = estilizarBotao("Item");
		JButton btnFugir = estilizarBotao("Fugir");
		btnContinuar = estilizarBotao("Continuar");

		painelComandos.add(btnAtaque);
		painelComandos.add(btnHabilidade);
		painelComandos.add(btnItem);
		painelComandos.add(btnFugir);

		// --- CAIXA 3: Status da Party (Direita) ---
		JPanel painelStatus = criarPainelAzul();
		painelStatus.setLayout(new GridLayout(combate.getHerois().size(), 1, 0, 5));

		for (Heroi h : combate.getHerois()) {
			JPanel linhaHeroi = new JPanel(new GridLayout(1, 3, 5, 0));
			linhaHeroi.setBackground(AZUL_RPG);

			JLabel lblNomeHeroi = new JLabel(h.getNome());
			lblNomeHeroi.setForeground(BRANCO);
			lblNomeHeroi.setFont(new Font("SansSerif", Font.BOLD, 14));

			JProgressBar barraHP = new JProgressBar(0, h.getVida().getMaxima());
			barraHP.setForeground(new Color(50, 205, 50)); // Verde
			barraHP.setBackground(Color.DARK_GRAY);
			barraHP.setStringPainted(true);

			JProgressBar barraMP = new JProgressBar(0, h.getMana().getMaxima());
			barraMP.setForeground(new Color(30, 144, 255)); // Azul
			barraMP.setBackground(Color.DARK_GRAY);
			barraMP.setStringPainted(true);

			linhaHeroi.add(lblNomeHeroi);
			linhaHeroi.add(barraHP);
			linhaHeroi.add(barraMP);
			painelStatus.add(linhaHeroi);

			// Adiciona nas listas para atualizarmos depois
			labelsNomesHerois.add(lblNomeHeroi);
			barrasHPHerois.add(barraHP);
			barrasMPHerois.add(barraMP);
		}

		painelHUD.add(painelLog);
		painelHUD.add(painelComandos);
		painelHUD.add(painelStatus);

		this.add(painelHUD, BorderLayout.SOUTH);

		// ==========================================
		// 3. EVENTOS DOS BOTÕES
		// ==========================================
		btnAtaque.addActionListener(e -> {
			this.habilidadeArmada = -1;
			String nomeAtaque = combate.getHeroiAtual().getAtaquePadrao().getNome();
			logBatalha.append("\n> " + nomeAtaque + " selecionado. Clique em um alvo!");
		});

		btnHabilidade.addActionListener(e -> {
			List<Habilidade> habilidadesHeroi = combate.getHeroiAtual().getHabilidades();
			if (habilidadesHeroi.isEmpty()) {
				logBatalha.append("\n> Nenhuma habilidade disponível!");
				return;
			}
			this.habilidadeArmada = 0;
			logBatalha.append("\n> " + habilidadesHeroi.get(0).getNome() + " selecionada. Clique em um alvo!");
		});

		btnItem.addActionListener(e -> {
			Heroi heroiTurno = combate.getHeroiAtual();

			String[] opcoesMenu = heroiTurno.getInventario().obterMenuDeItens();

			if (opcoesMenu.length == 0) {
				logBatalha.append("\n> A mochila de " + heroiTurno.getNome() + " está vazia!");
				return;
			}

			String escolhido = (String) JOptionPane.showInputDialog(this, "Escolha um item:",
					"Mochila de " + heroiTurno.getNome(), JOptionPane.PLAIN_MESSAGE, null, opcoesMenu, opcoesMenu[0]);

			if (escolhido != null) {
				int indiceItem = Integer.parseInt(escolhido.split(" ")[0]);

				// Assim como no ataque, o Combate já resolve sozinho se o
				// turno dos monstros deve entrar em seguida.
				ResultadoAcao resultado = combate.processarAcaoHeroiItem(indiceItem);
				aposAcao(resultado);
			}
		});

		btnFugir.addActionListener(e -> {
			ResultadoAcao resultado = combate.fugir();
			aposAcao(resultado);
		});

		btnContinuar.addActionListener(e -> {
			janela.concluirCombate(combate.getEstado());
		});

		atualizarStatus(); // Carrega os valores e a cor do turno pela primeira vez
	}

	// ---------------------------------------------------
	// MÉTODOS DE DESIGN E LÓGICA
	// ---------------------------------------------------

	private JPanel criarPainelAzul() {
		JPanel painel = new JPanel();
		painel.setBackground(AZUL_RPG);
		painel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BRANCO, 3),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
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
	
	// Tudo o que a tela precisa fazer depois de qualquer ação que gasta o turno:
	// mostrar o que aconteceu, redesenhar as barras e ver se a batalha acabou.
	private void aposAcao(ResultadoAcao resultado) {
		logBatalha.append("\n> " + resultado.getMensagem());
		atualizarStatus();
		verificarFimDeJogo();
	}

	private void atualizarStatus() {
		for (int i = 0; i < combate.getHerois().size(); i++) {
			Heroi h = combate.getHerois().get(i);

			barrasHPHerois.get(i).setValue(h.getVida().getAtual());
			barrasHPHerois.get(i).setString("HP: " + h.getVida().getAtual());

			barrasMPHerois.get(i).setValue(h.getMana().getAtual());
			barrasMPHerois.get(i).setString("MP: " + h.getMana().getAtual());

			// Destaca de quem é o turno com uma seta amarela e texto.
			// Compara o próprio objeto (==) em vez de procurar a posição com
			// indexOf: quando o grupo perde, getHeroiAtual() é null, e
			// List.of(...).indexOf(null) lança NullPointerException.
			if (h == combate.getHeroiAtual()) {
				labelsNomesHerois.get(i).setForeground(Color.YELLOW);
				labelsNomesHerois.get(i).setText("▶ " + h.getNome());
			} else {
				labelsNomesHerois.get(i).setForeground(BRANCO);
				labelsNomesHerois.get(i).setText(h.getNome());
			}
		}
		for (int i = 0; i < combate.getMonstros().size(); i++) {
		    Monstro m = combate.getMonstros().get(i);
		    barrasHPMonstros.get(i).setValue(m.getVida().getAtual());
		}
		
	}

	private void verificarFimDeJogo() {
		EstadoBatalha estado = combate.getEstado();
		if (estado == EstadoBatalha.EM_ANDAMENTO) {
			return;
		}

		String mensagemFinal = switch (estado) {
		case VITORIA -> "Vitória! Inimigos derrotados.";
		case DERROTA -> "Derrota! A party foi aniquilada.";
		case FUGA -> "O grupo fugiu da batalha.";
		case EM_ANDAMENTO -> "";
		};

		logBatalha.append("\n\n=== " + mensagemFinal + " ===");

		painelComandos.removeAll();
		painelComandos.add(btnContinuar);
		painelComandos.revalidate();
		painelComandos.repaint();
	}

}
