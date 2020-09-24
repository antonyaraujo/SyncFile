package Modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antony
 */
public class Leitor extends Thread {
    File arquivo;    
    Semaforo semaforo;    
    boolean estado;    
    String nome;
    Semaphore mutex;
    Semaphore acessoArquivo;
    int leitores;        
    String conteudo;
    
    public Leitor(String conteudo, File arquivo, Semaphore mutex, Semaphore acessoArquivo, int leitores, int i){
        this.arquivo = arquivo;
        estado = false;        
        nome = "Leitor " + (i+1);        
        this.leitores = leitores;
        this.mutex = mutex;
        this.acessoArquivo = acessoArquivo;
        this.conteudo = conteudo;
    }
    
    @Override
    public void run(){
        try {                   
            System.out.println("Je suis une lecteur de le [" + arquivo.getName() + "]");            
            
            FileReader fr = new FileReader(arquivo);
            BufferedReader br = new BufferedReader(fr);            
            
            while(br.ready())
                conteudo += br.readLine();                                                                                 
            
            System.out.println(conteudo);
            br.close();
            fr.close();   
                                                
            //semaforo.sairDaFila(this); 
            //interrupt();
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Leitor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Leitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }           
    
    public File getArquivo(){
        return arquivo;
    }
    
    public String getConteudo(){
        return conteudo;
    }    
    
    public int getAcessoArquivo(){
        return acessoArquivo.availablePermits();
    }        
    
    public void adquirirPermissao(){
        try {
            mutex.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Leitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        leitores++;
                        
        System.out.println(nome + ": " + arquivo.getName() + " Esperando permissão de leitura!!!");
        if(leitores==1){
            try {
                acessoArquivo.acquire();                
            } catch (InterruptedException ex) {
                Logger.getLogger(Leitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        estado = true;
        System.out.println(nome + ": " + arquivo.getName() + " Consegui a permissão!!!");
        System.out.println(nome + " está bloqueado");
        mutex.release();
        //this.start();
    }
    
    public void liberarPermissao() {        
                
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
        }
        leitores--;
        
        System.out.println(nome + ": " + arquivo.getName() + " Esperando liberação de leitura!!!");        
        if (leitores == 0) {
            acessoArquivo.release();
        }
        
        System.out.println("Leitura finalizada " + nome + ": libera a permissao.");
        System.out.println(nome + " está desbloqueado!!\n");
        estado = false;
        mutex.release();
    }

    public boolean getEstado() {
        return estado;
    }
        
}
