package Controle;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import Modelo.Semaforo;
import Modelo.Leitor;
import Modelo.Escritor;
import Modelo.Observable;
import Modelo.Observer;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Sincronizador extends Thread implements Observable {

    private ArrayList<Observer> observers = new ArrayList();
    File[] arquivos;
    String conteudoAtual, conteudoTemporario;
    ArrayList fila;
    boolean modificacao;
    Semaforo s;
    int valorTemporario;

    public Sincronizador(File[] arquivos, String conteudoAtual) {
        this.arquivos = arquivos;
        this.conteudoAtual = conteudoAtual;
        fila = new ArrayList();
        s = new Semaforo(fila);
        modificacao = false;
        conteudoTemporario = "";
        valorTemporario = 0;
    }

    public String getConteudoAtual() {
        return conteudoAtual;
    }

    public void setConteudoAtual(String novoConteudo) {
        conteudoAtual = novoConteudo;        
    }
    
    public void setModificado(){
        modificacao = true;
    }

    @Override
    public void run() {
        while (true) {
            if (modificacao) {
                Escritor e = new Escritor(conteudoAtual, fila, arquivos[new Random().nextInt(3)]);
                try {
                    s.entrarNaFila(e);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }
                modificacao = false;
            } else {
                Leitor leitor1 = new Leitor(arquivos, fila);
                Leitor leitor2 = new Leitor(arquivos, fila);

                while (true) {
                    if (leitor2.getNumArquivo() != leitor1.getNumArquivo()) {
                        break;
                    } else {
                        leitor2 = new Leitor(arquivos, fila);
                    }
                }

                try {
                    s.entrarNaFila(leitor1);
                    s.entrarNaFila(leitor2);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }



                if ((conteudoAtual.equals(leitor1.getConteudo())) && !(conteudoAtual.equals(leitor2.getConteudo()))) {
                    Escritor e = new Escritor(leitor1.getConteudo(), fila, leitor2.getArquivo());
                    try {
                        s.entrarNaFila(e);
                        System.out.println("Entrei na diferença");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (!(conteudoAtual.equals(leitor1.getConteudo())) && conteudoAtual.equals(leitor2.getConteudo())) {
                    Escritor e = new Escritor(leitor2.getConteudo(), fila, leitor1.getArquivo());
                    try {
                        s.entrarNaFila(e);                       
                        System.out.println("Entrei na diferença");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (!(leitor1.getConteudo().equals(""))) {
                    conteudoTemporario = leitor1.getConteudo();
                    valorTemporario = leitor1.getNumArquivo() + 1;
                    notifyObservers();
                }

                if (!(leitor2.getConteudo().equals(""))) {
                    conteudoTemporario = leitor2.getConteudo();
                    valorTemporario = leitor2.getNumArquivo() + 1;
                    notifyObservers();
                }                                
             

                if(leitor1.getConteudo().equals(leitor2.getConteudo()))
                    try {
                        sleep((int)Math.random()*1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer ob : observers) {
            ob.update(conteudoTemporario, valorTemporario);
        }
    }
}
