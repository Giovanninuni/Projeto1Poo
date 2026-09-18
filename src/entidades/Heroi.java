package entidades;

import atributos.Mana;
import atributos.Experiencia;
import itens.Inventario;
import itens.Item;
import ataques.Ataque;
import ataques.Habilidade;
import acoes.ResultadoAcao;
import equipamentos.DepositoEquipamentos;
import equipamentos.Equipamento;
import equipamentos.Equipagem;
import equipamentos.TipoEquipamento;
import java.util.ArrayList;
import java.util.List;

public abstract class Heroi extends Personagem {
   private Mana mana;
   private Inventario inventario;
   private List<Habilidade> habilidades;
   private Ataque ataquePadrao;
   private Experiencia experiencia;
   private Equipagem equipagem;

   public Heroi(String nome, int vidaMaxima, int ataqueBase, int defesa, int manaMaxima, Ataque ataquePadrao) {
      super(nome, vidaMaxima, ataqueBase, defesa);
      this.mana = new Mana(manaMaxima);
      this.inventario = new Inventario(10);
      this.habilidades = new ArrayList<>();
      this.ataquePadrao = ataquePadrao;
      this.experiencia = new Experiencia();
      this.equipagem = new Equipagem();
   }

   public void ganharXp(int quantidade) {
       int niveisGanhos = experiencia.ganhar(quantidade);

       for (int i = 0; i < niveisGanhos; i++) {
           getVida().aumentarMaxima(10);
           getMana().aumentarMaxima(5);
           getAtributos().aumentarAtaque(2);
           getAtributos().aumentarDefesa(1);
       }
   }

   public int getNivel() {
       return experiencia.getNivel();
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

   // Retira o equipamento do deposito do grupo antes de chamar este metodo.
   // Se o heroi nao puder usar o equipamento (restricao de classe), ou se
   // ele estiver trocando um item que ja estava equipado, o equipamento
   // que sair do slot (o antigo, ou o proprio novo em caso de rejeicao)
   // volta pro deposito automaticamente.
   public ResultadoAcao equipar(Equipamento novo, DepositoEquipamentos deposito) {
       if (!novo.podeSerUsadoPor(this)) {
           deposito.adicionar(novo);
           return new ResultadoAcao(false, this.getNome() + " não pode usar " + novo.getNome() + "!");
       }

       Equipamento antigo = equipagem.equipar(getAtributos(), novo);

       if (antigo != null) {
           deposito.adicionar(antigo);
       }

       return new ResultadoAcao(true, this.getNome() + " equipou " + novo.getNome() + "!");
   }

   // Esvazia um slot sem equipar outra coisa no lugar; o item removido
   // volta pro deposito do grupo (nao "some").
   public ResultadoAcao desequipar(TipoEquipamento tipo, DepositoEquipamentos deposito) {
       Equipamento removido = equipagem.desequipar(getAtributos(), tipo);

       if (removido == null) {
           return new ResultadoAcao(false, this.getNome() + " não tem nada equipado nesse slot!");
       }

       deposito.adicionar(removido);
       return new ResultadoAcao(true, this.getNome() + " removeu " + removido.getNome() + "!");
   }

   public Equipagem getEquipagem() {
       return this.equipagem;
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
