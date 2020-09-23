package Modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antony
 */
public class Escritor extends Thread{
    String conteudo;
    Semaforo semaforo;
    File f;
    
    public Escritor(String conteudo, ArrayList fila, File f){
        this.conteudo = conteudo;
        semaforo = new Semaforo(fila);
        this.f = f;        
    }
    
    public void run(){
        try {            
            System.out.println("Je suis une écrivain");            
            System.out.println("Escritor: Entrei na Fila");
            
            FileWriter fw = new FileWriter(f, false);
            BufferedWriter bw = new BufferedWriter(fw);            
            bw.write(conteudo);
            bw.close();
            fw.close();
            
            
            System.out.println("Escrevi o que tinha de escrever e sai");            
            semaforo.sairDaFila(this);
                        
        } catch (IOException ex) {
            Logger.getLogger(Escritor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Escritor.class.getName()).log(Level.SEVERE, null, ex);
        }                   
    }
}
