/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import Modelo.Arquivo;
import Modelo.Permissoes;
import java.util.Random;

/**
 *
 * @author antony
 */
public class Leitor extends Thread{
    private final Arquivo arquivo;
    private final Permissoes permissoes;
    private String conteudo;    
    
    public Leitor(Arquivo arquivo, Permissoes permissoes){
        this.arquivo = arquivo;
        conteudo = "";        
        this.permissoes = permissoes;
    }
    
    /**
     * Executa uma thread com comandos de leitura ao ser invocado
     */
    @Override
    synchronized public void run(){
        while(true){
            if(!arquivo.getPego()){ // verifica se o arquivo nao esta sendo usado por outro leitor
                arquivo.pegar(); // Se nao tiver, o arquivo e pego
                permissoes.adquirirPermissaoLeitor(); // adquire-se as permissoes de leitura
                System.out.println("Leitor: Permissão adquirida - Entrando na zona crítica");
                arquivo.lerArquivo(); // le-se o arquivo
                conteudo = arquivo.getConteudo(); // armazena-se o arquivo na variavel conteudo
                sleep();
                permissoes.liberarPermissaoLeitor(); // libera-se as permissoes do arquivo
                System.out.println("Leitor: Finalizei a minha leitura\n");
                break; // o loop de verificacao e quebrado
            }
        }
        arquivo.soltar();
    }
    
    /**
     * retorna o conteudo do arquivo lido armazenado na variavel conteudo do tipo String
     * @return conteudo - variavel que armazena o conteudo do arquivo
     */
    public String getConteudo(){
        return conteudo;
    }
        
    /**
     * Confere se o objeto Arquivo que o Leitor possui esta atualizado
     * @return true - se o arquivo estiver atualizado
     * @retorn false - se o arquivo estiver desatualizado
     */
    public boolean isAtualizado(){
        return arquivo.isAtualizado();       
    }
    
    private void sleep() {
		try {
			sleep(new Random().nextInt(10)*1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    
    
}
