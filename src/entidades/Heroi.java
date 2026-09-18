package entidades;

import atributos.Mana;
import itens.Inventario;
import itens.Item;
import ataques.Ataque;
import ataques.Habilidade;
import acoes.ResultadoAcao;
import java.util.ArrayList;
import java.util.List;

public abstract class Heroi extends Personagem {
   private Mana mana;
   private Inventario inventario;
   private List<Habilidade> habilidades;
   private Ataque ataquePadrao;

   public Heroi(String nome, int vidaMaxima, int ataqueBase, int defesa, int manaMaxima, Ataque ataquePadrao) {
      super(nome, vidaMaxima, ataqueBase, defesa);
      this.mana = new Mana(manaMaxima);
      this.inventario = new Inventario(10);
      this.habilidades = new ArrayList<>();
      this.ataquePadrao = ataquePadrao;
   }

   public ResultadoAcao usarItem(int indiceItem) {
	    if(this.inventario.estaVazio()) {
	        return new ResultadoAcao(false, "Sua mochila está vazia!");
	    }

	    // O herói tenta pegar o item no índice que a interface gráfica (Swing) mandou
	    Item itemEscolhido = this.inventario.consumirItem(indiceItem);

	    if(itemEscolhido != null) {

	        ResultadoAcao resultado = itemEscolhido.consumir(this, this);
	        return resultado;

	    }

	    return new ResultadoAcao(false, "Item inválido selecionado!");
	}

   public Ataque getAtaquePadrao() {
	   return this.ataquePadrao;
   }

   public Mana getMana() {
      return this.mana;
   }

   public Inventario getInventario() {
	   return this.inventario;
   }

   public List<Habilidade> getHabilidades() {
       return this.habilidades;
   }

   public void aprenderHabilidade(Habilidade novaHabilidade) {
       this.habilidades.add(novaHabilidade);
   }

}
