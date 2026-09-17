/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetos;

import habilidades.ResultadoAcao;
import itens.Item;
import java.util.List;
import entidades.Heroi;

/**
 *
 * @author laeds
 */
public class Bau {
    private boolean fechado = true;
    private int x;
    private int y;
    private List<Item> itens;
    
    public Bau(int x, int y, List<Item> itens){
        this.x = x;
        this.y = y;
        this.itens = itens;
    }
    
    public ResultadoAcao abrir(Heroi usuario){
        if(fechado){
            fechado = false;
            
            if(itens.isEmpty()){
                return new ResultadoAcao(true, "O baú foi aberto, mas dentro dele não havia nada!");
            }
            
            StringBuilder log = new StringBuilder("O baú foi aberto e voce encontrou: ");
            for(Item i: itens){
                usuario.getInventario().adicionarItem(i);
                log.append(i.getNome()).append(", ");
            }
            
            log.append("!");
            return new ResultadoAcao(true, log.toString());
        }
        return new ResultadoAcao(false, "O baú já está aberto!");
    }
    
    
    
    public boolean isFechado(){
        return this.fechado;
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
}
