/*«Copyright 2006, 2007 Yann Arthur Nicolas»
 *www.merlinsource.com
 *yannart@gmail.com
 *
 * This file is part of SquaresImg.
 *
 * SquaresImg is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * SquaresImg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package gui;

import calc.GeneraImagen;
import java.awt.image.BufferedImage;

/**
 *
 * @author  Yann Arthur Nicolas
 */
public class GeneracionProgreso extends javax.swing.JDialog implements Runnable{
    
    private Thread mythread;
    private GeneraImagen generaimagen;
    private Frame parent;
    
    public GeneracionProgreso(Frame parent, boolean modal, GeneraImagen generaimagen) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
        this.generaimagen = generaimagen;
        mythread = new Thread(this);
        mythread.start();
        generaimagen.calcula();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jProgressBar = new javax.swing.JProgressBar();
        jButtonCancelar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(java.util.ResourceBundle.getBundle("lang").getString("generating"));

        jButtonCancelar.setText(java.util.ResourceBundle.getBundle("lang").getString("abort"));
        jButtonCancelar.setToolTipText(java.util.ResourceBundle.getBundle("lang").getString("abort_generation"));
        jButtonCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonCancelarMouseClicked(evt);
            }
        });

        jLabel1.setText(java.util.ResourceBundle.getBundle("lang").getString("please_wait"));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addContainerGap(106, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .add(jProgressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(116, Short.MAX_VALUE)
                .add(jButtonCancelar)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jProgressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonCancelar)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    private void jButtonCancelarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCancelarMouseClicked
        mythread = null;
        generaimagen.stopCalcula();
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCancelarMouseClicked
    
    public void run(){
        int progress = 0;
        while(mythread != null){
            try {
                mythread.sleep(50);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            progress = generaimagen.getProgress();
            if(progress == 1000){
                this.jProgressBar.setValue(100);
                try {
                    mythread.sleep(50);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                mythread = null;
                this.setVisible(false);
                parent.muestraImagenFinal(generaimagen.getImagenFinal());
                this.dispose();
                
            }else{
                this.jProgressBar.setValue(progress);
            }
        }
    }
    
    public void setGeneraimagen(GeneraImagen generaimagen) {
        this.generaimagen = generaimagen;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JProgressBar jProgressBar;
    // End of variables declaration//GEN-END:variables
    
}