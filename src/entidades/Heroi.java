package entidades;

import java.util.Random;
import itens.Inventario;
import itens.Item;

public class Heroi extends Personagem {
   private int mana;
   private int manaMaxima;
   private Inventario inventario;
   private Random random = new Random();

   public Heroi(String nome, int vidaMaxima, int ataqueBase, int defesa, int manaMaxima) {
      super(nome, vidaMaxima, ataqueBase, defesa);
      this.manaMaxima = manaMaxima;
      this.mana = manaMaxima;
      this.inventario = new Inventario(5);
   }
   
   @Override
   public void atacar(Personagem alvo) { 
	   golpeDeEspada(alvo);              
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
   
   public void recuperarMana(int quantidade) {
       this.mana = Math.min(this.manaMaxima, this.mana + quantidade);
       System.out.printf("%n%s recuperou mana! (HP: %d/%d)%n", this.getNome(), this.mana, this.manaMaxima);
   }

   public int getMana() {
      return this.mana;
   }

   public int getManaMaxima() {
      return this.manaMaxima;
   }
   
   public Inventario getInventario() {
	   return this.inventario;
   }
   
   public boolean usarItem(int indiceUsuario) {
	   if(getInventario().estaVazio()) {
		   System.out.println("Sua mochila esta vazia!");
		   return false;
   		}
   	
   	getInventario().listarItens();
   	System.out.print("Escolha o número do item para usar: ");
   	
   	Item itemEscolhido = getInventario().consumirItem(indiceUsuario);
   	if(itemEscolhido != null) {
   		itemEscolhido.usar(this);
   		return true;
   	}
   	return false;
   }
   
   
}
