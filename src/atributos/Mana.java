package atributos;

public class Mana {
	private int atual;
	private int maxima;
	
	public Mana(int maxima) {
		this.maxima = maxima;
		this.atual = maxima;
	}
	
	public boolean temSuficiente(int custo) {
		return custo > 0 && this.atual >= custo;
	}
	
	public boolean gastar(int custo) {
		if(temSuficiente(custo)) {
			this.atual = Math.max(0, this.atual - custo);
			return true;
		}
		return false;
	}
	
	public void restaurar(int quantidade) {
		if(quantidade > 0) {
			this.atual = Math.min(this.maxima, this.atual + quantidade);
		}
	}

    public int getAtual() {
    	return atual; 
    }
    public int getMaxima() {
    	return maxima; 
    }
	
	@Override
	public String toString() {
		return atual + "/" + maxima;
	}
}
