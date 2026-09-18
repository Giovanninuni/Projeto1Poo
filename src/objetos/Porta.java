/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetos;

import acoes.ResultadoAcao;
import entidades.Heroi;

/**
 *
 * @author laeds
 */
public class Porta {
    private int x;
    private int y;
    private boolean fechada = true;
    
    public Porta(int x, int y){
        this.x = x;
        this.y = y;
    }
    //adicionar mecanica de porta trancada depois
    public ResultadoAcao abrir(Heroi usuario){
        this.fechada = false;
        return new ResultadoAcao(true, "A porta foi aberta");
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }
    
    public boolean isFechada(){
        return this.fechada;
    }
}
