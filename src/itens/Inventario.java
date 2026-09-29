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
		if(temEspaco()) {
			this.itens.add(item);
			return true;
		}
		else {
			return false;
		}
	}

	public Item consumirItem(int indiceUsuario) {
		int indiceReal = indiceUsuario - 1;

		if(indiceReal >= 0 && indiceReal < this.itens.size()) {
			return this.itens.remove(indiceReal);
		}
		else {
			return null;
		}
	}
	
    public String[] obterMenuDeItens() {
        // Cria um array de Strings do tamanho do inventário (vazio se não houver itens)
        String[] menu = new String[this.itens.size()];

        for (int i = 0; i < this.itens.size(); i++) {
            // Vai criar textos como: "1 - Poção de Vida" ou "2 - Éter"
            menu[i] = (i + 1) + " - " + this.itens.get(i).getNome();
        }
        
        return menu;
    }
	
	public boolean estaVazio(){
		return this.itens.isEmpty();
	}
	
	public boolean temEspaco() {
		return itens.size() < capacidadeMaxima;
	}
	
	public int getQuantidadeItens() {
		return this.itens.size();
	}
}
