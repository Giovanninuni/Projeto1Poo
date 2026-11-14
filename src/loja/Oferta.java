package loja;

import java.util.function.Supplier;

import contratos.Negociavel;

import core.ContadorObjetos;

// Usar o T (type) permite que a oferta de item e equipamento usem o mesmo código
// T extends Negociavel restringe o T para qualquer tipo que seja negociavel (item e equipamento)
// Dentro do <> usa o extends mesmo sendo interface
// A própria Oferta também é Negociavel: a tela da loja mostra nome, descrição e
// preço dela sem nunca tocar na amostra
public class Oferta<T extends Negociavel> implements Negociavel {
    
        { ContadorObjetos.registrar(this); }
        
	private final Supplier<T> fabrica; // Guarda a receita (o método do catálogo), não o objeto: cada compra fabrica um novo
	private final T amostra; 		   // Só para exibir nome e valor; nunca vai para um comprador. getAmostra() só é visível no pacote loja

	Oferta(Supplier<T> fabrica){
		this.fabrica = fabrica;
		this.amostra = fabrica.get(); // Cria o objeto como amostra na vitrine
	}

	T criar() {
		return fabrica.get(); // Objeto novo a cada compra
	}

	@Override
	public String getNome() {
		return amostra.getNome();
	}

	@Override
	public String getDescricao() {
		return amostra.getDescricao();
	}

	@Override
    public int getValor() {
    	return amostra.getValor();
    }

    T getAmostra() {
    	return amostra;
    }
}
