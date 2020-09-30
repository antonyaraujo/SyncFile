/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.Arquivo;
import Modelo.Observable;
import Modelo.Observer;
import Modelo.Permissoes;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antony
 */
    public class Sincronizador extends Thread implements Observable{
    Arquivo[] arquivos;
    boolean modificado;
    String conteudoAtual;
    Permissoes permissoes;
    ArrayList<Observer> observers;
    String conteudoTemporario;
    int valor;
    
    public Sincronizador(Arquivo[] arquivos){
        this.arquivos = arquivos;
        modificado = false;
        conteudoAtual = "";
        permissoes = new Permissoes();
        observers = new ArrayList();
        conteudoTemporario = "";
        valor = 0;
    }
    
    /**
     * Altera o valor da variavel conteudo do tipo String
     * @param conteudo  // armazena o conteudo mais atualizado dos arquivos
     */
    public void setConteudoAtual(String conteudo){
        this.conteudoAtual = conteudo;
    }
    
    /**
     * Muda o valor da variavel modificado para true
     * Indica que um arquivo aleatorio deve ser modificado
     */
    public void setModificado(){
        this.modificado = true; 
    }
            
    @Override
    public void run(){
        while(true){
            if(modificado){ // se existir modificacao a ser feita
                int numero = new Random().nextInt(3);
                Escritor escritor = new Escritor(arquivos[numero], conteudoAtual, permissoes);
                escritor.start();                                
                try {
                    escritor.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }                
                escritor.atualizarArquivo();
                for(int i = 0; i < 3; i++){
                    if(i != numero)
                        arquivos[i].desatualizar();
                }
                modificado = false;
            } else { // se nao existir modificacao, deve-se fazer somente sincronizacao
                int n1 = new Random().nextInt(3);
                int n2 = new Random().nextInt(3);
                while(true){
                    if(n1 != n2)
                        break;
                    else{
                        n1 = new Random().nextInt(3);
                        n2 = new Random().nextInt(3);
                    }
                }
                
                Leitor leitor1 = new Leitor(arquivos[n1], permissoes);
                Leitor leitor2 = new Leitor(arquivos[n2], permissoes);
                leitor1.start();
                try {
                    leitor1.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }
                leitor2.start();
                try {
                    leitor2.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                /**
                 * se o arquivo 1 estiver atualizado e o segundo nao
                 * atualiza-se o arquivo 2 com o conteudo do arquivo 1
                 */
                if(leitor1.isAtualizado() && !leitor2.isAtualizado()){ 
                    
                    Escritor escritor = new Escritor(arquivos[n2], leitor1.getConteudo(), permissoes);
                    escritor.start();                    
                    try {
                        escritor.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    escritor.atualizarArquivo();
                }
                
                /**
                 * se o arquivo 2 estiver atualizado e o segundo nao
                 * atualiza-se o arquivo 1 com o conteudo do arquivo 2
                 */
                if(!leitor1.isAtualizado() && leitor2.isAtualizado()){
                    Escritor escritor = new Escritor(arquivos[n1], leitor2.getConteudo(), permissoes);
                    escritor.start();
                    try {
                        escritor.join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    escritor.atualizarArquivo();
                }
                
                conteudoTemporario = leitor1.getConteudo();
                valor = n1+1;
                notifyObservers(); // notifica-se a interface que um dos arquivos foi atualizado
                conteudoTemporario = leitor2.getConteudo();
                valor = n2+1;
                notifyObservers(); // notifica-se a interface que um dos arquivos foi atualizado
                                
            
            }
            if(!modificado){
                try {
                    sleep(new Random().nextInt(10)*1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }        
    }
    
    /**
     * Adiciona um objeto para observar modificacoes feitas no Sincronizador
     * @param observer 
     */
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Remove um objeto que ja observa o Sincronizador
     * @param observer 
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifica os observadores do sincronizador de modificacoes que ocorreram
     */
    @Override
    public void notifyObservers() {
        for (Observer ob : observers) {
            ob.update(conteudoTemporario, valor);
        }
   }    
    
}
