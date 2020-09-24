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
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Sincronizador extends Thread implements Observable {

    private ArrayList<Observer> observers = new ArrayList();
    String conteudoAtual, conteudoTemporario;
    boolean modificacao;
    int valorTemporario;
    Leitor[] leitores;
    Escritor[] escritores;
    Semaforo semaforo;

    public Sincronizador(String conteudoAtual, Leitor[] leitores, Escritor[] escritores) {
        this.conteudoAtual = conteudoAtual;
        modificacao = true;
        conteudoTemporario = "";
        valorTemporario = 0;
        this.leitores = leitores;
        this.escritores = escritores;
        this.semaforo = semaforo;
    }

    public String getConteudoAtual() {
        return conteudoAtual;
    }

    public void setConteudoAtual(String novoConteudo) {
        conteudoAtual = novoConteudo;
        modificacao = true;
    }

    @Override
    public void run() {
        while (true) {
            if (modificacao) {
                int numero = 0;
                while (true) {
                    numero = new Random().nextInt(3);
                    if (!escritores[numero].getEstado()) {
                        escritores[numero].adquirirPermissao();
                        System.out.println("Só uma chegou aqui com " + numero);
                        break;
                        
                    } 
                }
                escritores[numero].setConteudo(conteudoAtual);
                escritores[numero].start();                   
                try {
                    escritores[numero].join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
                }
                escritores[numero].liberarPermissao();
                modificacao = false;
            } else {
                int numero1 = 0;
                int numero2 = 0;
                while (true) {
                    numero1 = new Random().nextInt(3);
                    if (!leitores[numero1].getEstado()) {
                        leitores[numero1].adquirirPermissao();
                    }
                    numero2 = new Random().nextInt(3);
                    if (!leitores[numero2].getEstado()) {
                        leitores[numero2].adquirirPermissao();
                    }
                    
                    if (leitores[numero1].getEstado() && !(leitores[numero2].getEstado())) {
                        leitores[numero1].liberarPermissao();
                    }
                    if (!(leitores[numero1].getEstado()) && (leitores[numero2].getEstado())) {
                        leitores[numero2].liberarPermissao();
                    }
                    if (leitores[numero1].getEstado() && leitores[numero2].getEstado()) {
                        break;
                    }
                }

                
                    leitores[numero1].start();
                    leitores[numero1].liberarPermissao();                    
                    leitores[numero2].start();                    
                    leitores[numero2].liberarPermissao();                                    
                                                

                if (conteudoAtual.equals(leitores[numero1].getConteudo()) && !(conteudoAtual.equals(leitores[numero2].getConteudo()))) {
                    conteudoTemporario = leitores[numero1].getConteudo();
                    valorTemporario = numero1;
                    notifyObservers();
                    while (true) {
                        if (!escritores[numero2].getEstado()) {
                            escritores[numero2].adquirirPermissao();
                            escritores[numero2].setConteudo(leitores[numero1].getConteudo());
                            escritores[numero2].start();
                            escritores[numero2].liberarPermissao();
                            conteudoTemporario = leitores[numero2].getConteudo();
                            valorTemporario = numero2;
                            notifyObservers();
                            break;
                        }
                    }
                }

                if (!(conteudoAtual.equals(leitores[numero1].getConteudo())) && (conteudoAtual.equals(leitores[numero2].getConteudo()))) {
                    conteudoTemporario = leitores[numero2].getConteudo();
                    valorTemporario = numero2;
                    notifyObservers();
                    while (true) {
                        if (!escritores[numero1].getEstado()) {
                            escritores[numero1].adquirirPermissao();
                            escritores[numero1].setConteudo(leitores[numero2].getConteudo());
                            escritores[numero1].start();
                            escritores[numero1].liberarPermissao();
                            conteudoTemporario = leitores[numero1].getConteudo();
                            valorTemporario = numero1;
                            notifyObservers();
                            break;
                        }
                    }
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
