package main;

//import core.Tela;
import entidades.Personagem;
import itens.PocaoMana;
import itens.PocaoVida;
import entidades.Heroi;
import entidades.Goblin;
import core.Combate;


public class Main {
	
	public static void main(String[] args) {
		//new Tela();
		Heroi heroi = new Heroi("Arthur", 100, 18, 5, 30);
		Personagem goblin = new Goblin("Goblin Ladrão");
		Combate combate = new Combate(heroi, goblin);
		heroi.getInventario().adicionarItem(new PocaoVida("Poção de Vida Menor", "Cura 25 HP", 25));
		heroi.getInventario().adicionarItem(new PocaoMana("Poção de Mana Menor", "Restaura 15 MP", 15));
		combate.iniciar();
		}
	}
