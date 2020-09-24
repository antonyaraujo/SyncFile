package Modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antony
 */
public class Leitor extends Thread {
    File[] arquivos;
    String conteudoArquivo;
    Semaforo s;
    int posArquivo;
    
    public Leitor(File[] arquivos, ArrayList listaGeral){
        this.arquivos = arquivos;  
        conteudoArquivo = "";
        s = new Semaforo(listaGeral);       
        posArquivo = new Random().nextInt(3);        
    }
    
    @Override
    public void run(){
        try {                        
            System.out.println("Je suis une lecteur de le [" + arquivos[posArquivo].getName() + "]");            
            
            FileReader fr = new FileReader(arquivos[posArquivo]);
            BufferedReader br = new BufferedReader(fr);
            String conteudo = "";
            
            while(br.ready()){                
                if(!(br.readLine().equals("\n")))
                    conteudo += br.readLine();                 
            }                                    
            conteudoArquivo = conteudo;                                                                
            
            System.out.println(conteudoArquivo);
            br.close();
            fr.close();   
                        
            s.sairDaFila(this);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Leitor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Leitor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Leitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
    public int getNumArquivo(){
        return posArquivo;
    }
    
    public File getArquivo(){
        return arquivos[posArquivo];
    }
    
    public String getConteudo(){
        return conteudoArquivo;
    }                
}
