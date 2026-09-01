package entidades;

public abstract class Personagem {
    private String nome;
    private int vida;
    private int vidaMaxima;
    private int ataqueBase;
    private int defesa;

    public Personagem(String nome, int vidaMaxima, int ataqueBase, int defesa) {
        this.nome = nome;
        this.vidaMaxima = vidaMaxima;
        this.vida = vidaMaxima; // Inicia com a vida cheia
        this.ataqueBase = ataqueBase;
        this.defesa = defesa;
    }

    // Método abstrato: toda subclasse DEVE implementar sua própria versão
    public abstract void atacar(Personagem alvo);

    // Método de negócio com cálculo de dano e encapsulamento
    public void receberDano(int danoBruto) {
        int danoEfetivo = Math.max(1, danoBruto - this.defesa); // No mínimo 1 de dano
        this.vida = Math.max(0, this.vida - danoEfetivo);
        
        System.out.printf("%s recebeu %d de dano! (HP: %d/%d)%n", 
            this.nome, danoEfetivo, this.vida, this.vidaMaxima);
    }

    public void curar(int quantidade) {
        this.vida = Math.min(this.vidaMaxima, this.vida + quantidade);
        System.out.printf("%n%s recuperou vida! (HP: %d/%d)%n", this.nome, this.vida, this.vidaMaxima);
    }
    

    public boolean estaVivo() {
        return this.vida > 0;
    }

    // Getters
    public String getNome() { 
    	return nome; 
    }
    public int getVida() { 
    	return vida; 
    }
    public int getVidaMaxima() { 
    	return vidaMaxima; 
    }
    public int getAtaqueBase() { 
    	return ataqueBase; 
    }
    public int getDefesa() { 
    	return defesa; 
    }
    
}