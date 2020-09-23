/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.ArrayList;
import java.lang.Thread;

/**
 *
 * @author antony
 */
public class Semaforo {

    Object atual;
    int valor;
    ArrayList filaGeral;

    public Semaforo(ArrayList filaGeral) {
        atual = null;
        this.filaGeral = filaGeral;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public void entrarNaFila(Object objeto) throws InterruptedException {
        filaGeral.add(objeto); // adiciona objeto a fila
        if (atual == null) {
            atual = objeto; // caso nao exista objeto na fila, o atual torna-se o objeto adicionado
            if (objeto instanceof Leitor) {
                ((Leitor) objeto).start();  // Se for um leitor o mesmo e executado
            } else {
                ((Escritor) objeto).start(); // Se for um escritor o mesmo e executado
            }
        } else { // se ja existir um objeto executando
            if (atual instanceof Leitor) { // verifica-se se quem esta executando e um leitor
                if (objeto instanceof Leitor) { // se o atual for um leitor e o objeto que entrou na fila tbm for um leitor, entao o que entrou tbm pode ser executado
                    ((Leitor) objeto).start();
                } else { // se o atual for um leitor mas o que entrou for um escritor, o leitor precisa sair para dar prioridade ao escritor                        
                    sairDaFila(atual); // O leitor e interrompido                    
                    ((Escritor) objeto).sleep((int)Math.random() * 1000);
                }
            } else {
                if (objeto instanceof Leitor) {
                    ((Leitor) objeto).sleep((int)Math.random() * 1000);  // Se o atual for um escritor e o inserido um leitor, entao esse deve esperar e tentar novamente
                } else {
                    ((Escritor) objeto).sleep(1000);  // Se o atual for um escritor e o inserido um escritor, este deve esperar ate que o escritor atual termine sua escrita                
                }
            }

        }

        /*if(objeto instanceof Leitor){
                if(atual == null){
                    ((Leitor) objeto).start();  
                    atual = objeto;
                } else {
                    if(atual instanceof Leitor)
                       ((Leitor)objeto).start();
                    else{
                        ((Leitor) objeto).sleep(2);
                        System.out.println("Sou leitor e não consegui ser executado, re-entrando na fila");
                    }
                }                
            } else {
                if(atual == null){
                    ((Escritor) objeto).start();
                     atual = objeto;
                } else{
                    if(atual instanceof Escritor){
                        ((Escritor) objeto).sleep(2);
                        System.out.println("Sou escritor e não consegui ser executado, re-entrando na fila");
                    }
                    else{
                        int i = 0;
                        while(i < filaGeral.size()){
                            if(atual instanceof Leitor)
                                sairDaFila(filaGeral.get(i));                                 
                            else                                
                                i = filaGeral.size();                            
                            i++;
                        }                        
                         //((Escritor)objeto).start();
                         //atual = objeto;
                    }        
                        
                }
            } */
    }

    public void sairDaFila(Object objeto) throws InterruptedException {                
        filaGeral.remove(objeto);
        if (objeto == atual) {
            if (objeto instanceof Escritor) {
                ((Escritor) objeto).interrupt();
                atual = filaGeral.get(0);
            } else {
                ((Leitor) atual).interrupt();
                for (int i = 0; i < filaGeral.size(); i++) {
                    if (filaGeral.get(i) instanceof Leitor) {
                        ((Leitor) filaGeral.get(i)).interrupt();
                        filaGeral.remove(filaGeral.get(i));
                    } else{
                        atual = filaGeral.get(i);
                        break;                    
                    }
                }
            }
            
            if (atual instanceof Escritor) 
                ((Escritor) atual).start();                                              
        } else {            
            if(objeto instanceof Leitor)
                ((Leitor) objeto).interrupt();    
            else
                ((Escritor)objeto).interrupt();
          
        }

    }

}
