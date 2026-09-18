/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetos;

import acoes.ResultadoAcao;
import itens.Item;
import java.util.List;
import entidades.Heroi;
import equipamentos.DepositoEquipamentos;
import equipamentos.Equipamento;

/**
 *
 * @author laeds
 */
public class Bau {
    private boolean fechado = true;
    private int x;
    private int y;
    private List<Item> itens;
    private List<Equipamento> equipamentos;

    public Bau(int x, int y, List<Item> itens, List<Equipamento> equipamentos){
        this.x = x;
        this.y = y;
        this.itens = itens;
        this.equipamentos = equipamentos;
    }

    // NOTA (2026-09-18): o Laedson vai mexer nesse fluxo pra mostrar o item
    // antes de escolher o heroi (ver anotação de projeto). Quando isso for
    // refeito, os equipamentos dessa lista também precisam entrar nessa
    // revisão: eles não vão pra mochila de ninguém, vão direto pro
    // DepositoEquipamentos do grupo (não dependem de qual heroi abriu o baú).
    public ResultadoAcao abrir(Heroi usuario, DepositoEquipamentos deposito){
        if(isFechado()){
            fechado = false;

            if(itens.isEmpty() && equipamentos.isEmpty()){
                return new ResultadoAcao(true, "O baú foi aberto, mas dentro dele não havia nada!");
            }

            StringBuilder log = new StringBuilder("O baú foi aberto e voce encontrou: ");
            //pra cada item dentro do bau o item é adicionado e seu nome vai pra o log da mensagem
            for(Item i: itens){
                usuario.getInventario().adicionarItem(i);
                log.append(i.getNome()).append(", ");
            }

            //equipamentos nao vao pra mochila de ninguem, vao pro deposito do grupo
            //Provisorio isso aqui laedson a gente pode mudar depois
            for(Equipamento e: equipamentos){
                deposito.adicionar(e);
                log.append(e.getNome()).append(" (foi para o depósito de equipamentos), ");
            }

            log.append("!");
            return new ResultadoAcao(true, log.toString());
        }
        return new ResultadoAcao(false, "O baú já está aberto!");
    }
    
    
    
    public boolean isFechado(){
        return this.fechado;
    }

    // Sugestão de refatoração (ainda não feita): Bau, Porta e Masmorra.Encontro
    // todos têm x/y e hoje são buscados por posição em métodos separados e
    // duplicados dentro de Masmorra (getBauAdjacenteAoHeroi, existeBau,
    // getEncontroNaPosicaoDoHeroi fazem o mesmo tipo de loop). Dá pra extrair
    // uma interface comum:
    //   interface Posicionavel { int getX(); int getY(); }
    // implementada por Bau, Porta e Encontro, e escrever UM único par de
    // métodos genéricos reaproveitável pelos três, por exemplo:
    //   Posicionavel encontrarAdjacente(List<? extends Posicionavel> lista, int heroiX, int heroiY)
    //   boolean existeNaPosicao(List<? extends Posicionavel> lista, int x, int y)
    // Assim, quando a Porta for integrada de fato, ela reaproveita esses
    // métodos em vez de precisar de um getPortaAdjacenteAoHeroi()/existePorta()
    // copiado igual aos de Bau.
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
}
