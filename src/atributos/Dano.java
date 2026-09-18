package atributos;

public class Dano {
	private int valor;
	private boolean critico;
	private TipoDano tipo;
	
	public enum TipoDano {
		FISICO, MAGICO, PERFURANTE
	}
	
	public Dano(int valor, boolean critico, TipoDano tipo) {
		this.valor = valor;
		this.critico = critico;
		this.tipo = tipo;
	}
	
	public int getValor() {
		return this.valor;
	}
	
	public boolean isCritico() {
		return critico; 
	}
	
    public TipoDano getTipo() {
    	return tipo; 
    }
}
