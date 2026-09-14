package entidades;

import atributos.Vida;
import habilidades.ResultadoAcao;
import atributos.Atributos;
import atributos.Dano;

public abstract class Personagem {
    private String nome;
    private Vida vida;
    private Atributos atributos;

    public Personagem(String nome, int vidaMaxima, int ataqueBase, int defesa) {
        this.nome = nome;
        this.vida = new Vida(vidaMaxima); // Inicia com a vida cheia
        this.atributos = new Atributos(ataqueBase, defesa);
    }
    
    public abstract ResultadoAcao atacar(Personagem alvo);

    // Método de negócio com cálculo de dano e encapsulamento
    public int receberDano(Dano dano) {
        int danoEfetivo;
        
        if(dano.getTipo() == Dano.TipoDano.PERFURANTE) {
        	danoEfetivo = Math.max(1, dano.getValor());
        }
        else {
        	danoEfetivo = Math.max(1, dano.getValor() - this.atributos.getDefesa());
        }
        
        this.vida.reduzir(danoEfetivo);
        return danoEfetivo; 
        
    }

    public boolean estaVivo() {
    	return !this.vida.estaZerado();
    }
    

    // Getters
    public String getNome() { 
    	return nome; 
    }
    public Vida getVida() { 
    	return this.vida; 
    }
    public Atributos getAtributos() { 
    	return this.atributos; 
    }
 
}