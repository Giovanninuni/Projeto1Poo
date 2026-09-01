package core;

import entidades.Personagem;
import itens.Item;
import entidades.Heroi;
import java.util.Scanner;

public class Combate {
	private Heroi heroi;
	private Personagem inimigo;
	private Scanner scanner;
	
	public Combate(Heroi heroi, Personagem inimigo) {
		this.heroi = heroi;
		this.inimigo = inimigo;
		this.scanner = new Scanner(System.in);
	}
	
	public void executarTurnoJogador() {
		System.out.println("\nEscolha sua ação:");
        System.out.println("1. Golpe de Espada");
        System.out.println("2. Bola de Fogo (7 MP)");
        System.out.println("3. Abrir Mochila");
        System.out.print("> ");
        
        int opcao = scanner.nextInt();
        
        if(opcao == 3) {
        	if(heroi.getInventario().estaVazio()) {
        		System.out.println("Sua mochila esta vazia! Você perdeu o turno procurando itens.");
        		return;
        	}
        	
        	heroi.getInventario().listarItens();
        	System.out.print("Escolha o número do item para usar: ");
        	int indiceItem = scanner.nextInt();
        	
        	Item itemEscolhido = heroi.getInventario().consumirItem(indiceItem);
        	if(itemEscolhido != null) {
        		itemEscolhido.usar(heroi);
        	}
        }
        else {
        	heroi.atacar(inimigo, opcao);
        }
	}
	
	public boolean iniciar() {
		System.out.printf("%n=== Batalha Iniciada: %s vs %s ===%n", heroi.getNome(), inimigo.getNome());
		
		while(heroi.estaVivo() && inimigo.estaVivo()) {
			executarTurnoJogador();
			
			if(inimigo.estaVivo()) {
				System.out.printf("%n--- Turno de %s ---%n", inimigo.getNome());
				inimigo.atacar(heroi);
			}
		}
		
		if (heroi.estaVivo()) {
            System.out.printf("%nVocê derrotou o %s!%n", inimigo.getNome());
            return true; // Herói venceu
        } else {
            System.out.println("\nVocê tombou em batalha...");
            return false; // Herói morreu
        }
    }
}
	

