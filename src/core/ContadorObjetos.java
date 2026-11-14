package core;

import java.util.Map;
import java.util.TreeMap;

public class ContadorObjetos {

    // pacote -> (classe -> quantidade)
    private static final Map<String, Map<String, Integer>> contagem = new TreeMap<>();

    private ContadorObjetos() { }

    public static void registrar(Object objeto) {
        Class<?> classe = objeto.getClass();
        String pacote = classe.getPackage().getName();
        String nome = classe.getSimpleName();

        contagem.computeIfAbsent(pacote, p -> new TreeMap<>())
                .merge(nome, 1, Integer::sum);
    }

    public static int getTotal() {
        int total = 0;
        for (Map<String, Integer> porClasse : contagem.values()) {
            for (int qtd : porClasse.values()) {
                total += qtd;
            }
        }
        return total;
    }

    public static void imprimirRelatorio() {
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
