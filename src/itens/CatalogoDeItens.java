package itens;

/**
 * Ponto unico de criacao dos itens do jogo -- mesmo padrao do
 * equipamentos.CatalogoDeEquipamentos. Fica no mesmo pacote de Item,
 * PocaoVida e PocaoMana de proposito: os construtores delas sao
 * package-private, entao SO um metodo daqui consegue chamar "new
 * PocaoVida(...)"/"new PocaoMana(...)" -- ninguem de fora (Masmorra,
 * JanelaPrincipal etc.) consegue inventar um nome/quantidade diferente
 * pro mesmo item sem querer.
 *
 * Cada metodo retorna uma instancia NOVA a cada chamada, pelo mesmo
 * motivo do catalogo de equipamentos: dois locais diferentes que dao o
 * mesmo item nao devem compartilhar o mesmo objeto.
 */
public class CatalogoDeItens {

    public static Item pocaoDeVida() {
        return new PocaoVida("Poção de Vida", "Restaura 30 de vida", 30, null);
    }

    // Variante mais forte da mesma poção -- mesmo comportamento (consumir),
    // só muda o número e, futuramente, o sprite. Por isso não é uma classe
    // nova, é só outra chamada de PocaoVida com valores diferentes.
    public static Item pocaoDeVidaMaior() {
        return new PocaoVida("Poção de Vida Maior", "Restaura 50 de vida", 50, null);
    }

    public static Item pocaoDeMana() {
        return new PocaoMana("Poção de Mana", "Restaura 30 de mana", 30, null);
    }
}
