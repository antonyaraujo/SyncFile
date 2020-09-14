/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.List;

/**
 *
 * @author antony
 */
public class Semaforo {
     private int contador;
     private int controlador;
     
     Semaforo(){
         this.contador = 0;
         this.controlador = 1;
     }
     
     Semaforo(int contador){
         this.contador = contador;
         this.controlador = 1;
     }
     
     public void espera(){
         if(contador>0)
            contador--;
     }
     
     public void sinal(List listaEspera){
         if(!listaEspera.isEmpty() || contador == 0){
             listaEspera.get(0);
         } else {
             contador++;
         }
     }
     
     public void pthread_mutex_lock(){}
     
     public void pthread_mutex_unlock(){}


}
