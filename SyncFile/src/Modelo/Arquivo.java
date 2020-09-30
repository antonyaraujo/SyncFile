/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antony
 */

public class Arquivo {
    private File arquivo; 
    private FileReader fr; 
    private BufferedReader br;
    private FileWriter fw;
    private BufferedWriter bw;
    private boolean atualizado; //  booleano que indica se o arquivo esta atualizado ou nao
    private String conteudo; // Armazena o conteudo mais atual do arquivo
    private boolean pego; // booleano que inidca se o arquivo esta sendo usado por algum sincronizador
    
    public Arquivo(File arquivo) throws FileNotFoundException, IOException{
        this.arquivo = arquivo;                
        atualizado = false;        
        pego = false;
    }
    
    /**
     * Metodo que realiza a leitura do arquivo
     * E armazena o conteudo do arquivo na variavel conteudo do tipo String
     */
    public void lerArquivo(){
        try {
            fr = new FileReader(arquivo);
            br = new BufferedReader(fr);
            String auxiliar = "";
            while (br.ready()) {
                auxiliar += br.readLine()+ "\n";
            }
            conteudo = auxiliar;
            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    /**
     * Metodo que permite a escrita no arquivo a partir do parametro String fornecido
     * @param novoConteudo novo conteudo a ser escrito no arquivo 
     */
    public void escreverArquivo(String novoConteudo){
        try{
            fw = new FileWriter(arquivo);        
            bw = new BufferedWriter(fw);
            bw.write(novoConteudo);
            bw.close();
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * retorrna um objeto do tipo File 
     * @return // arquivo que o objeto contem
     */
    public File getArquivo() {
        return arquivo;
    }

    /**
     * Modifica o arquivo que o objeto arquivo contem
     * @param arquivo  // o novo arquivo a ser armazenado no objeto
     */
    public void setArquivo(File arquivo) {
        this.arquivo = arquivo;
    }

    /**
     * retorna um valor booleano que indica se o arquivo esta atualizado ou nao
     * @return true - se o arquivo estiver atualizado
     * @return false - se o arquivo estiver desatualizado
     */
    public boolean isAtualizado() {
        return atualizado;
    }

    /**
     * Indica que o arquivo esta atualizado
     * define o valor da variavel booleana atualizado como verdadeiro
     */
    public void atualizar() {
        this.atualizado = true;
    }
    
    /**
     * Indica que o arquivo esta desatualizado
     * define o valor da variavel booleana atualizado como falso
     */
    public void desatualizar(){
        this.atualizado = false;
    }

    /**
     * Retorna o conteudo do arquivo
     * @return retorna string armazenada na variavel conteudo
     */
    public String getConteudo() {
        return conteudo;
    }

    /**
     * Altera o conteudo do arquivo a partir de um parametro string recebido
     * @param conteudo 
     */
    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
    /*
     * retorna um valor booleano que indica se o arquivo foi pego por algum sincronizador
     * @return true - se o objeto Arquivo tiver sido pego por algum sincronizador
     * @return false - se o objeto Arquivo nao tiver sido pego por algum sincronizador
    */
    public boolean getPego(){
        return pego;
    }
    
    /**
     * Altera o valor da variavel booleana pego para verdadeiro
     * Passa a indicar que o objeto Arquivo foi pego por algum sincronizador
     */
    public void pegar(){
        pego = true;
    }
    
    /**
     * Altera o valor da variavel booleana pego para falso
     * Passa a indicar que o objeto Arquivo foi solto ou nao estar pego
     * por algum sincronizador
     */
    public void soltar(){
        pego = false;
    }                
  
}
