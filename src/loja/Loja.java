package loja;

import java.util.List;

import atributos.Ouro;
import contratos.Negociavel;
import contratos.ResultadoAcao;
import entidades.Heroi;
import equipamentos.DepositoEquipamentos;
import equipamentos.Equipamento;
import itens.Item;

public class Loja {
	private final String nome;
	private final List<Oferta<Item>> ofertasDeItens;
	private final List<Oferta<Equipamento>> ofertasDeEquipamentos;
	
	Loja(String nome, List<Oferta<Item>> itens, List<Oferta<Equipamento>> equipamentos){
		this.nome = nome;
		this.ofertasDeItens = itens;
		this.ofertasDeEquipamentos = equipamentos;
	}
	
	public int precoDeCompra(Negociavel mercadoria) {
		return mercadoria.getValor();
	}
	
	public int precoDeVenda(Negociavel mercadoria) {
		return mercadoria.getValor()/2; // Preço de venda é metade do valor da mercadoria
	}
	
	public ResultadoAcao comprarItem(int indiceOferta, Heroi comprador, Ouro ouro) {
		int indiceReal = indiceOferta - 1;
		if (indiceReal < 0 || indiceReal >= ofertasDeItens.size()) {
		    return new ResultadoAcao(false, "Oferta inválida!");
		}
		
		Oferta<Item> oferta = ofertasDeItens.get(indiceReal);
		int preco = precoDeCompra(oferta.getAmostra());
		
		// Checa o que pode falhar sem mexer em nada
		if (!comprador.getInventario().temEspaco()) {
			return new ResultadoAcao(false, "A mochila de " + comprador.getNome() + " está cheia!");
		}
		
		// Cobra: O Ouro checa e desconta de uma vez só
		if (!ouro.gastar(preco)) {
			return new ResultadoAcao(false, "Ouro insuficiente!");
		}
		
		//Entrega
		comprador.getInventario().adicionarItem(oferta.criar());
		return new ResultadoAcao(true, comprador.getNome() + " comprou " + oferta.getNome() + "!");
	}
	
    public ResultadoAcao comprarEquipamento(int indiceOferta, Ouro ouro, DepositoEquipamentos deposito) {
    	int indiceReal = indiceOferta - 1;
    	if (indiceReal < 0 || indiceReal >= ofertasDeEquipamentos.size()) {
    	    return new ResultadoAcao(false, "Oferta inválida!");
    	}
    	
    	Oferta<Equipamento> oferta = ofertasDeEquipamentos.get(indiceReal);
		int preco = precoDeCompra(oferta.getAmostra());

		// O depósito não tem limite, então não há espaço a checar
		// Cobra: O Ouro checa e desconta de uma vez só
		if (!ouro.gastar(preco)) {
			return new ResultadoAcao(false, "Ouro insuficiente!");
		}

		//Entrega
		deposito.adicionar(oferta.criar());
		return new ResultadoAcao(true, "A equipe comprou " + oferta.getNome() + "!");
    }
    
    public String getNome() {
    	return nome;
    }
    
    public List<Oferta<Item>> getOfertasDeItens() { 
    	return ofertasDeItens;
    }
    
    public List<Oferta<Equipamento>> getOfertasDeEquipamentos() {
    	return ofertasDeEquipamentos;
    }
}
