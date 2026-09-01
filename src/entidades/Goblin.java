package entidades;

public class Goblin extends Personagem{
	
	public Goblin(String nome) {
		super(nome, 50, 12, 2);
	}
	
	@Override
	public void atacar(Personagem alvo) {
		System.out.printf("%s avança sorrateiramente com uma adaga enferrujada!%n", getNome());
		alvo.receberDano(getAtaqueBase());
	}
}
