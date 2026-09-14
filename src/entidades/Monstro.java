package entidades;

public abstract class Monstro extends Personagem{
	private int xpConcedida;
	private int ouroDropado;
	
	public Monstro(String nome, int vidaMaxima, int ataqueBase, int defesa, int xpConcedida, int ouroDropado) {
		super(nome, vidaMaxima, ataqueBase, defesa);
		this.xpConcedida = xpConcedida;
		this.ouroDropado = ouroDropado;
	}
	
	public int getXpConcedida(){
		return this.xpConcedida;
	}
	
	public int getOuroDropado() {
		return this.ouroDropado;
	}
}
