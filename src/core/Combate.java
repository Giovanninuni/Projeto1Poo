package core;

import entidades.Personagem;
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
        System.out.print("> ");
        
        int opcao = scanner.nextInt();
        
        heroi.atacar(inimigo, opcao);
	}
}
