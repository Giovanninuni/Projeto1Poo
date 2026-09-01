package entidades;

import java.util.Random;

public class Heroi extends Personagem {
   private int mana;
   private int manaMaxima;
   private Random random = new Random();

   public Heroi(String nome, int vidaMaxima, int ataqueBase, int defesa, int manaMaxima) {
      super(nome, vidaMaxima, ataqueBase, defesa);
      this.manaMaxima = manaMaxima;
      this.mana = manaMaxima;
   }
   
   @Override
   public void atacar(Personagem alvo) { // ainda nao entendi muito bem por que ter esse alem 
	   golpeDeEspada(alvo);              // de evitar o erro de compilação
   }
   
   public void atacar(Personagem alvo, int tipoAtaque){
      switch (tipoAtaque) {
         case 1:
            golpeDeEspada(alvo);
            break;
         case 2:
            bolaDeFogo(alvo);
            break;
         default:
            System.out.println("Opção inválida! Usando ataque padrão.");
            golpeDeEspada(alvo);
      }
   }

   private void golpeDeEspada(Personagem alvo) {
      boolean critico = this.random.nextDouble() < (double)0.25F;
      int dano = critico ? this.getAtaqueBase() * 2 : this.getAtaqueBase();
      if (critico) {
         System.out.printf("[Crítico!] %s desfere um golpe avassalador em %s!%n", this.getNome(), alvo.getNome());
      } else {
         System.out.printf("%s ataca %s com sua espada!%n", this.getNome(), alvo.getNome());
      }

      alvo.receberDano(dano);
   }

   private void bolaDeFogo(Personagem alvo) {
      int custoMana = 7;
      if (this.mana >= custoMana) {
         this.mana -= custoMana;
         int danoMagico = this.getAtaqueBase() + 15;
         System.out.printf("%s conjura uma BOLA DE FOGO flamejante em %s! (Mana restante: %d/%d)%n", this.getNome(), alvo.getNome(), this.mana, this.manaMaxima);
         alvo.receberDano(danoMagico);
      } else {
         System.out.println("Mana insuficiente! Você tenta atacar com a espada no desespero...");
         this.golpeDeEspada(alvo);
      }

   }

   public int getMana() {
      return this.mana;
   }

   public int getManaMaxima() {
      return this.manaMaxima;
   }
}
