/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antony
 */
public class Permissoes {

    private Semaphore acessoRecurso = new Semaphore(1);
    private Semaphore acessoLeitores = new Semaphore(1);
    private Semaphore filaEspera = new Semaphore(1);
    private int numLeitores = 0;

    
    /**
     * Metodo responsavel por adquirir a permissao de leitura
     * para os leitores, caso nao exista escritor atuando
     */
    public void adquirirPermissaoLeitor() {
        try {
            filaEspera.acquire(); // Bloqueia a fila de espera [ninguem entra e ninguem sai]
            acessoLeitores.acquire(); // Bloqueia a regiao compartilhada dos leitores
            numLeitores++; // incrementa a quantidade do numero de leitores
            if (numLeitores == 1) { // Se for primeiro objeto Leitor, entao:
                acessoRecurso.acquire(); // a secao critica e bloqueada para todos os leitores
            }            
            acessoLeitores.release(); // Libera o acesso de novos leitores
            filaEspera.release(); // Libera a fila de espera
        } catch (InterruptedException ex) {
            Logger.getLogger(Permissoes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /**
     * Metodo responsavel por liberar a seçao critica     
     */
    public void liberarPermissaoLeitor(){
        try {
            acessoLeitores.acquire(); // Bloqueia a regiao compartilhada dos leitores
            numLeitores--; // decrementa o numero de leitores
            if(numLeitores == 0) // verifica se este e o unico leitor na regiao, se for:
                acessoRecurso.release(); // libera o acesso ao Recurso
            acessoLeitores.release(); // Desbloqueia a regiao compartilhada dos leitores
        } catch (InterruptedException ex) {
            Logger.getLogger(Permissoes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
/**
 * Metodo responsavel por adquirir a permissao de escrita para um objeto Escritor
 */    
    public void adquirirPermissaoEscritor() {
        try {
            filaEspera.acquire(); // Bloqueia a fila de espera
            acessoRecurso.acquire(); // adquire a secao critica para si
            filaEspera.release(); // Libera a fila de espera
        } catch (InterruptedException ex) {
            Logger.getLogger(Permissoes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Metodo responsavel por liberar a seçao critica usada por um Escritor
     */
    public void liberarPermissaoEscritor(){
        acessoRecurso.release();
    }


}
