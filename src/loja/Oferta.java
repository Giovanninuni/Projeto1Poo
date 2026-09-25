package loja;

import java.util.function.Supplier;

import contratos.Negociavel;

// Usar o T (type) permite que a oferta de item e equipamento usem o mesmo código
// T extends Negociavel restringe o T para qualquer tipo que seja negociavel (item e equipamento)
// Dentro do <> usa o extends mesmo sendo interface
public class Oferta<T extends Negociavel>{
	private final Supplier<T> fabrica; // Guarda a receita (o método do catálogo), não o objeto: cada compra fabrica um novo
	private final T amostra; 		   // Só para exibir nome e valor; nunca vai para um comprador. getAmostra() só é visível no pacote loja

	Oferta(Supplier<T> fabrica){
		this.fabrica = fabrica;
		this.amostra = fabrica.get(); // Cria o objeto como amostra na vitrine
	}
	
	T criar() {
		return fabrica.get(); // Objeto novo a cada compra
	}
	
	public String getNome() {
		return amostra.getNome(); 
	}
    public int getValor() {
    	return amostra.getValor(); 
    }
    
    T getAmostra() {
    	return amostra;
    }
}
