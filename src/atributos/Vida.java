package atributos;

public class Vida {
	private int atual;
	private int maxima;
	
	public Vida(int maxima) {
		this.maxima = maxima;
		this.atual = maxima;
	}
	
	public void reduzir(int quantidade) {
		this.atual = Math.max(0, this.atual - quantidade);
	}
	
	public void restaurar(int quantidade) {
		this.atual = Math.max(this.maxima, this.atual + quantidade);
	}
	
	public boolean estaZerado() {
		return this.atual <= 0;
	}
	
	public double getPorcentagem() {
		return (double) this.atual / this.maxima;
	}
	
	public int getAtual() {
		return this.atual;
	}
	
	public int getMaxima() {
		return this.maxima;
	}
	
	@Override
	public String toString() {
		return atual + "/" + maxima;
	}
}
