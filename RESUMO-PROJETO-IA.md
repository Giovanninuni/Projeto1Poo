# Contexto do projeto — RPG 2D em Java (POO)

Resumo preparado para continuar o trabalho em outra sessão/ferramenta de IA.
Se você está lendo isso no Claude Code: pode considerar tudo abaixo como
contexto já validado pelo usuário, não precisa re-perguntar o óbvio.

## Visão geral

- Projeto da disciplina de Programação Orientada a Objetos, em grupo.
- Gênero: RPG por turnos, estilo Final Fantasy / Pokémon clássico.
- Interface gráfica: Java Swing (JFrame + CardLayout + JPanel), sem engine externa.
- IDE usada pelo grupo: Eclipse.
- Autoria: o usuário fez as classes principais e o sistema de batalha
  (pacotes `atributos`, `entidades`, `habilidades`, `itens`, `core.Combate`).
  A masmorra original e a adição de um 2º herói / 2º inimigo de teste foram
  feitas por colegas de equipe, não pelo usuário.

## Arquitetura atual (pacotes)

- `atributos` — `Vida`, `Mana`, `Atributos` (ataque/defesa), `Dano` (+ enum `TipoDano`: FISICO/MAGICO/PERFURANTE).
- `entidades` — `Personagem` (abstrata) → `Heroi` e `Monstro` (abstrata) → `Goblin`, `Esqueleto`.
- `habilidades` — `Habilidade` (abstrata, padrão Strategy) → `GolpeEspada`, `BolaDeFogo`; `ResultadoAcao` (sucesso + mensagem).
- `itens` — `Item` (abstrata) → `PocaoVida`, `PocaoMana`; `Inventario`.
- `core` — `Combate` (motor de turnos, a classe principal do sistema de batalha).
- `mundo` — `Masmorra` (novo; modelo de mapa, ver abaixo).
- `gui` — `JanelaPrincipal` (troca de telas), `PainelMasmorra`, `PainelCombate`.
- `assets/sprites` já tem imagens de goblin, esqueleto e herói (ainda não usadas na masmorra, que hoje desenha retângulos coloridos).

## Bugs corrigidos nesta sessão (já testados e funcionando)

1. **Turno do 2º herói nunca era acionado pela GUI.** Existiam dois sistemas
   de turno sobrepostos em `Combate`: um antigo e morto (`atacarAlvoSelecionado`)
   e o usado de fato pela GUI (`processarAcaoHeroiHabilidade`). A GUI chamava
   `processarTurnoInimigos()` manualmente logo após CADA ação de herói,
   duplicando o turno dos monstros e nunca dando espaço pro fluxo correto.
   Correção: `avancarTurno()` agora é o único método que decide quando os
   monstros agem (só depois que todos os heróis vivos já agiram na rodada) e
   devolve o log como `String`; a GUI só exibe essa string, não decide mais nada.
   Removidos `atacarAlvoSelecionado()` e `processarTurnoInimigos()` (mortos/redundantes).

2. **`sortearHeroiVivo()` não sorteava de verdade** — sempre retornava o
   primeiro herói vivo da lista, então os monstros sempre miravam no mesmo
   herói. Corrigido com `java.util.Random` sobre uma lista filtrada só com
   os heróis vivos.

3. **Lógica de exploração estava inteira dentro da GUI.** `PainelMasmorra`
   guardava posição do herói/monstro, fazia detecção de colisão e até
   instanciava os monstros do encontro, tudo dentro do `keyPressed`. Extraída
   para `mundo.Masmorra` (pacote que já existia vazio no projeto — parece
   que tinha sido reservado pra isso). Agora `PainelMasmorra` só lê teclas,
   chama `masmorra.mover()`, pergunta `masmorra.getEncontroNaPosicaoDoHeroi()`
   e desenha o que o modelo devolve. Bônus: o mapa ganhou limites (antes dava
   pra andar infinitamente pra fora da tela).

## Identificado mas NÃO alterado ainda (decisões em aberto)

- Todo `Heroi` nasce com as mesmas 2 habilidades (`GolpeEspada` + `BolaDeFogo`)
  — Guerreiro e Mago são mecanicamente idênticos, só mudam os números.
- Nenhum XP/ouro é concedido ao vencer um combate (`Monstro` já guarda
  `xpConcedida`/`ouroDropado`, mas `Combate` nunca usa isso).
- `JanelaPrincipal` cria a party (`new Heroi(...)`) direto no construtor da GUI.
- `gui/Window.java` está vazia e não é usada em lugar nenhum — candidata a remoção.
- `PainelMasmorra`/`Masmorra` ainda tem só 1 sala com 1 encontro fixo (sem
  múltiplas salas, sem tiles de parede/colisão com cenário).

## Ideias de roadmap discutidas (ordem sugerida por esforço x impacto)

1. Diferenciar classes de herói (`Guerreiro`/`Mago` como subclasses de `Heroi`, cada uma com sua lista própria de habilidades iniciais).
2. Sistema de XP/nível ao vencer combates.
3. Mapa real: múltiplas salas, paredes, encontros aleatórios em vez de posição fixa.
4. Mais inimigos e habilidades (a base already suporta, é só criar subclasses).
5. Loja/economia usando o ouro dropado e o `Inventario` já existente.
6. Salvar/carregar jogo (serialização ou JSON).
7. Trocar os retângulos coloridos por sprites (assets já existem em `assets/sprites`).

## Preferências do usuário (importante)

- Está aprendendo Java/POO e quer **entender**, não só receber código pronto.
  Prefere que qualquer mudança venha acompanhada de explicação — inclusive de
  sintaxe básica quando pedir (ex: já pediu para explicar `for (Heroi h : herois)`,
  e a diferença entre `String` e `StringBuilder` no contexto do log de batalha).
- O projeto é um repositório git. Cuidado: a view de **Git Staging** do Eclipse
  tem botões de "Replace"/"Discard" que revertem mudanças não commitadas sem
  aviso claro — isso já causou perda de uma correção nesta sessão (foi refeita).
  O usuário prefere revisar diffs por `Compare With > Local History` ou
  `Compare With > HEAD Revision` no Eclipse, em vez da Git Staging view.

## Estado atual (validado pelo usuário)

Testado no Eclipse e funcionando: o turno alterna corretamente entre os dois
heróis, os monstros só atacam depois que toda a rodada de heróis termina, o
alvo dos monstros é sorteado aleatoriamente entre os heróis vivos, e o mapa
da masmorra tem limites reais.
