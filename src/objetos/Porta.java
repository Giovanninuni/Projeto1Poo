/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetos;

import contratos.ResultadoAcao;
import entidades.Grupo;
import core.ContadorObjetos;

/**
 *
 * @author laeds
 */
public class Porta {
    
    { ContadorObjetos.registrar(this); }
    
    private int x;
    private int y;
    private boolean fechada = true;
    private boolean trancada;
    
    public Porta(int x, int y, boolean trancada){
        this.x = x;
        this.y = y;
        this.trancada = trancada;
    }
    //adicionar mecanica de porta trancada depois
    public ResultadoAcao abrir(Grupo grupo){
        if(isFechada()){
            this.fechada = false;
            return new ResultadoAcao(true, "A porta foi aberta");
        }
        return new ResultadoAcao(false, "A porta já está aberta!");
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
