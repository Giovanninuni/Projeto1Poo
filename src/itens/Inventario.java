package itens;

import java.util.ArrayList;
import java.util.List;

public class Inventario {
	private List<Item> itens;
	private int capacidadeMaxima;
	
	public Inventario(int capacidadeMaxima) {
		this.capacidadeMaxima = capacidadeMaxima;
		this.itens = new ArrayList<>();
	}
	
	public boolean adicionarItem(Item item) {
		if(this.itens.size() < this.capacidadeMaxima) {
			this.itens.add(item);
			System.out.println(item.getNome() + " foi guardado na mochila.");
			return true;
		}
		else {
			System.out.println("Mochila cheia! Não foi possível pegar " + item.getNome());
			return false;
		}
	}
	
	public void listarItens() {
		if(this.itens.isEmpty()) {
			System.out.println("Sua mochila está vazia.");
			return;
		}
		
		System.out.println("=== MOCHILA ===");
		for(int i = 0; i < this.itens.size(); i++) {
			Item item = this.itens.get(i);
			System.out.printf("%d. %s - %s%n", (i + 1), item.getNome(), item.getDescricao());
		}
	}
	
	public Item consumirItem(int indiceUsuario) {
		int indiceReal = indiceUsuario - 1;
		
		if(indiceReal >= 0 && indiceReal < this.itens.size()) {
			return this.itens.remove(indiceReal);
		}
		else {
			System.out.println("\nPosição inválida!");
			return null;
		}
	}
	
	public boolean estaVazio(){
		return this.itens.isEmpty();
	}
	
	public int getQuantidadeItens() {
		return this.itens.size();
	}
}
