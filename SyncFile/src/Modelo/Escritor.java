package Modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antony
 */
public class Escritor extends Thread {

    String conteudo;    
    File f;
    boolean estado;
    String nome;    
    Semaphore acessoArquivo = new Semaphore(1);

    public Escritor(String conteudo, File f, int i, Semaphore acessoArquivo) {
        this.conteudo = conteudo;        
        this.f = f;
        estado = false;
        nome = "Escritor " + (i + 1);
        this.acessoArquivo = acessoArquivo;
    }

    public void run() {
        try {            
            System.out.println("Je suis une écrivain");
            System.out.println("Escritor: Entrei na Fila");

            FileWriter fw = new FileWriter(f, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(conteudo);
            bw.close();
            fw.close();
            
            System.out.println("Escrevi o que tinha de escrever e sai");            
            //semaforo.sairDaFila(this);                                                
            interrupt();
            
        } catch (IOException ex) {
            Logger.getLogger(Escritor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getAcessoArquivo(){
        return acessoArquivo.availablePermits();
    }

    public void adquirirPermissao() {
        System.out.println(nome + " esta esperando uma permissao para escita do arquivo");
        try {                        
            acessoArquivo.acquire();            
        } catch (InterruptedException ex) {
            Logger.getLogger(Escritor.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        estado = true;
        System.out.println(nome + " foi autorizado, acessando arquivo " + f.getName());
        System.out.println(nome + " esta escrevendo no arquivo" + f.getName());
    }

    public void liberarPermissao() {        
        System.out.println("Escrita finalizada, " + nome + " libera a permissao.");                  
        acessoArquivo.release(); 
        estado = false;
        
    }
    
    public boolean getEstado(){
        return estado;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }        
}
