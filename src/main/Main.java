package main;

//import core.Tela;
import entidades.Personagem;
import entidades.Heroi;
import entidades.Goblin;
import core.Combate;


public class Main {
	
	public static void main(String[] args) {
		//new Tela();
		Heroi guerreiro = new Heroi("Arthur", 100, 18, 5, 30);
		Personagem goblin = new Goblin("Goblin Ladrão");
		Combate combate = new Combate(guerreiro, goblin);
		
		System.out.println("--- ÍNICIO DE COMBATE DE TESTE ---");
		
		while(guerreiro.estaVivo() && goblin.estaVivo()) {
			System.out.println("\n--- Turno do Herói ---");
			combate.executarTurnoJogador();
			
			if(goblin.estaVivo()) {
				System.out.println("\n--- Turno do Goblin ---");
				goblin.atacar(guerreiro);
			}
		}
		
		System.out.println("\n--- FIM DE COMBATE ---");
		if(guerreiro.estaVivo()) {
			System.out.println("Vitória do Herói!");
		}
		else {
			System.out.println("O Herói foi derrotado!");
		}
	}
}
