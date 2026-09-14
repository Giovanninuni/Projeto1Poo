package entidades;

import atributos.Mana;
import itens.Inventario;
import itens.Item;
import habilidades.Habilidade;
import habilidades.BolaDeFogo;
import habilidades.GolpeEspada;
import habilidades.ResultadoAcao;
import java.util.ArrayList;
import java.util.List;

public class Heroi extends Personagem {
   private Mana mana;
   private Inventario inventario;
   private List<Habilidade> habilidades;

   public Heroi(String nome, int vidaMaxima, int ataqueBase, int defesa, int manaMaxima) {
      super(nome, vidaMaxima, ataqueBase, defesa);
      this.mana = new Mana(manaMaxima);
      this.inventario = new Inventario(10);
      
      this.habilidades = new ArrayList<>();
      this.habilidades.add(new GolpeEspada());
      this.habilidades.add(new BolaDeFogo());
   }
   
   @Override
   public ResultadoAcao atacar(Personagem alvo) {
       // O ataque básico do herói é simplesmente usar sua habilidade padrão (índice 0)
       return usarHabilidade(0, alvo);
   }
   
   public ResultadoAcao usarHabilidade(int indice, Personagem alvo) {
       if (indice >= 0 && indice < this.habilidades.size()) {
           Habilidade habilidade = this.habilidades.get(indice);
           return habilidade.executar(this, alvo);
       }
       return new ResultadoAcao(false, "Habilidade inválida ou não encontrada!");
   }
   
   public ResultadoAcao usarItem(int indiceItem) {
	    if(this.inventario.estaVazio()) {
	        return new ResultadoAcao(false, "Sua mochila está vazia!");
	    }
	    
	    // O herói tenta pegar o item no índice que a interface gráfica (Swing) mandou
	    Item itemEscolhido = this.inventario.consumirItem(indiceItem);
	    
	    if(itemEscolhido != null) {
	        itemEscolhido.usar(this); // Aplica a cura/mana
	        
	        // Retorna o sucesso e a mensagem para o JTextArea da tela imprimir
	        return new ResultadoAcao(true, getNome() + " consumiu um item da mochila!\n");
	    }
	    
	    return new ResultadoAcao(false, "Item inválido selecionado!");
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
