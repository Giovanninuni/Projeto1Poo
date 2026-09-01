package entidades;

public class Esqueleto extends Monstro{
	
	public Esqueleto(String nome) {
		super(nome, 70, 18, 0, 10, 8);
	}
	
	// implementar mecanica de tiro perfurante ignorando parte da defesa
	
	@Override
	public void atacar(Personagem alvo) {
		System.out.printf("%s avança com sua espada!%n", getNome());
		alvo.receberDano(getAtaqueBase());
	}
}
