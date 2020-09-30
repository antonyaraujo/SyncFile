/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.Arquivo;
import Modelo.Permissoes;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antony
 */
public class Escritor extends Thread{
    private final Arquivo arquivo;
    private final Permissoes permissoes;
    String conteudo;
    
    public Escritor(Arquivo arquivo, String conteudo, Permissoes permissoes){
        this.arquivo = arquivo;
        this.conteudo = conteudo;
        this.permissoes = permissoes;
    }
    
    /**
     * Executa uma thread com comandos de escrita no arquivo ao ser invocado
     */
    @Override
    synchronized public void run(){     
        while(true){
            if(!arquivo.getPego()){
                arquivo.pegar();
                permissoes.adquirirPermissaoEscritor();
                System.out.println("Escritor: Permissão adquirida - Entrando na zona crítica");
                arquivo.escreverArquivo(conteudo);

                sleep();
                atualizarArquivo();
                permissoes.liberarPermissaoEscritor();
                System.out.println("Escritor: Finalizei a leitura - Zona crítica liberada\n");
                break;
            }
        }
        arquivo.soltar();
    }
    
    /**
     * Modifica o conteudo a ser escrito no arquivo quanto o objeto-thread for executado
     * @param conteudo 
     */
    public void modificarArquivo(String conteudo){
        this.conteudo = conteudo;                
    }
    
    /**
     * Chama o metodo que atualiza o valor da variavel atualizado para true
     */
    public void atualizarArquivo(){
        arquivo.atualizar();
    }
    
    /**
     * Chama o metodo que atualiza o valor da variavel atualizado para false
     */
    public void desatualizarArquivo(){
        arquivo.desatualizar();
    }
    
    /**
     * Define um tempo em que a thread vai passar em espera (dormindo)
     */
    private void sleep() {
        try {
            sleep(new Random().nextInt(10)*1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
