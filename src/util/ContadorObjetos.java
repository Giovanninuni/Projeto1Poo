package util;

import java.util.Map;
import java.util.TreeMap;

/**
 * Conta quantos objetos de cada classe foram criados na execução,
 * agrupados por pacote. O relatório é impresso quando o jogo fecha
 * (shutdown hook na Main).
 *
 * Cada classe do topo de uma hierarquia tem um bloco de inicialização
 * { ContadorObjetos.registrar(this); }, que roda em todo "new" antes do
 * construtor. Como getClass() devolve a classe real do objeto, o bloco em
 * Personagem já conta Guerreiro, Mago, Goblin... cada um com o seu nome.
 *
 * Fica num pacote próprio, que não depende de nenhum outro do jogo:
 * assim atributos e contratos podem usá-lo sem criar dependência
 * circular com core.
 */
public class ContadorObjetos {

    // pacote -> (classe -> quantidade)
    private static final Map<String, Map<String, Integer>> contagem = new TreeMap<>();

    private ContadorObjetos() {
    }

    // synchronized porque o relatório é impresso pela thread do shutdown
    // hook, enquanto os objetos são criados na thread do Swing
    public static synchronized void registrar(Object objeto) {
        Class<?> classe = objeto.getClass();
        String pacote = classe.getPackage().getName();
        String nome = classe.getSimpleName();

        contagem.computeIfAbsent(pacote, p -> new TreeMap<>())
                .merge(nome, 1, Integer::sum);
    }

    public static synchronized int getTotal() {
        int total = 0;
        for (Map<String, Integer> porClasse : contagem.values()) {
            for (int qtd : porClasse.values()) {
                total += qtd;
            }
        }
        return total;
    }

    public static synchronized void imprimirRelatorio() {
        System.out.println("=== Objetos criados nesta execução ===");
        for (Map.Entry<String, Map<String, Integer>> p : contagem.entrySet()) {
            System.out.println("Pacote " + p.getKey());
            for (Map.Entry<String, Integer> c : p.getValue().entrySet()) {
                System.out.println("  " + c.getKey() + ": " + c.getValue());
            }
        }
        System.out.println("TOTAL: " + getTotal());
    }
}
