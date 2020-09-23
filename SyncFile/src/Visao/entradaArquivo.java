package Visao;

import Modelo.Observable;
import Modelo.Observer;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class entradaArquivo extends javax.swing.JPanel implements Observable{
    private String conteudo;
    private ArrayList<Observer> observers = new ArrayList();
    JFrame janela;
    boolean estado;
    
    public entradaArquivo(String conteudoArquivo, JFrame janela) {                        
        initComponents();
        conteudo = conteudoArquivo;           
        textoArquivo.setText(conteudo); 
        this.janela = janela;
        this.setVisible(true);
        estado = false;                
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        enviar = new javax.swing.JButton();
        cancelar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        textoArquivo = new javax.swing.JTextArea();

        enviar.setText("Enviar");
        enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviarActionPerformed(evt);
            }
        });

        cancelar.setText("Cancelar Modificação");
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });

        textoArquivo.setColumns(20);
        textoArquivo.setRows(5);
        jScrollPane1.setViewportView(textoArquivo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 171, Short.MAX_VALUE)
                        .addComponent(enviar)
                        .addGap(18, 18, 18)
                        .addComponent(cancelar))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelar)
                    .addComponent(enviar)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void enviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enviarActionPerformed
        conteudo = textoArquivo.getText();
        notifyObservers();
        this.enable(false);
        this.show(false);                
        janela.dispose();            
    }//GEN-LAST:event_enviarActionPerformed

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        estado = true;
        this.enable(false);
        this.show(false);        
        janela.dispose();        
    }//GEN-LAST:event_cancelarActionPerformed

    public String getConteudo(){
        return conteudo;
    }
    
    public boolean getEstado(){
        return estado;
    }  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelar;
    private javax.swing.JButton enviar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea textoArquivo;
    // End of variables declaration//GEN-END:variables

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
        for(Observer ob : observers){
            ob.update(conteudo);
        }
    }
    
}
