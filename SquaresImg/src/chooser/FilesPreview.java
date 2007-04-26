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

package chooser;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import javax.media.jai.JAI;
import javax.media.jai.RenderedImageAdapter;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author  Yann Arthur Nicolas
 */
public class FilesPreview extends javax.swing.JPanel implements PropertyChangeListener, Runnable{
    
    private ImageFileChooser chooser;
    private Thread mythread;
    private File previewfile = null;
    private File [] previewfiles = null;
    private boolean run_multiple_files = false;
    /** Creates new form FilesPreview */
    public FilesPreview(ImageFileChooser chooser) {
        initComponents();
        this.chooser = chooser;
        this.chooser.addPropertyChangeListener(this);
        verif.setVisible(false);
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)){
            
            File file = (File) e.getNewValue();
            if(file == null || chooser.isCuadros()) {
                clear();
                return;
            }else{
                preview.setVisible(true);
                verif.setVisible(false);
                if(preview.isSelected())
                    jPanelPreview.setVisible(true);
                setDetails(file);
            }
            
        }else if(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY.equals(prop)){
            clear();
            File [] files = chooser.getSelectedFiles();
            int num_files = files.length;
            
            if(num_files == 1){
                preview.setVisible(true);
                verif.setVisible(false);
                if(preview.isSelected())
                    jPanelPreview.setVisible(true);
                setDetails(files[0]);
            }else if(num_files > 1){
                jPanelPreview.setVisible(false);
                preview.setVisible(false);
                verif.setVisible(true);
                nom.setText(java.util.ResourceBundle.getBundle("lang").getString("several_images"));
                description.setText(java.util.ResourceBundle.getBundle("lang").getString("num_files"));
                taille.setText(""+num_files);
                run_multiple_files = true;
                if(verif.isSelected()){
                    previewfiles = files;
                    mythread = new Thread(this);
                    mythread.start();
                }else{
                    date.setText("");
                }
            }
        }else{
            clear();
        }
    }
    
    private void setDetails(File file){
        
        FileSystemView vueSysteme = FileSystemView.getFileSystemView();
        Locale locale = Locale.getDefault();
        NumberFormat nf = NumberFormat.getInstance(locale);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        
        
        nom.setText(vueSysteme.getSystemDisplayName(file));
        description.setText(vueSysteme.getSystemTypeDescription(file));
        description.setIcon(vueSysteme.getSystemIcon(file));
        String tailleFile = nf.format(file.length()/1024.0)+" Kb";
        taille.setText(tailleFile);
        String dateFile = dateFormat.format(new Date(file.lastModified()));
        date.setText(java.util.ResourceBundle.getBundle("lang").getString("modificated")+" "+dateFile);
        icone.setIcon(null);
        imagensize.setText("");
        if(Utils.isImagen(file) && preview.isSelected()){
            previewfile = file;
            run_multiple_files = false;
            mythread = new Thread(this);
            mythread.start();
        }else{
            previewfile = null;
        }
    }
    
    public static BufferedImage scale(BufferedImage bi, int maxside) {
        float width = bi.getWidth();
        float height = bi.getHeight();
        if(width < maxside && height< maxside)
            return bi;
        float scaleH = (float)maxside/width;
        float scaleV = (float)maxside/height;
        float scaleValue = scaleH < scaleV ? scaleH : scaleV;
        AffineTransform tx = new AffineTransform();
        tx.scale(scaleValue, scaleValue);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage biNew = new BufferedImage( (int) (width * scaleValue),(int) (height * scaleValue), bi.getType());
        return op.filter(bi, biNew);
    }
    
    public void run(){
        if(run_multiple_files){
            if(previewfiles == null || previewfiles.length <= 0)
                return;
            boolean samesize = true;
            RenderedImage img;
            RenderedImageAdapter ria;
            BufferedImage bi = null;
            int width = 0;
            int height = 0;
            if(Utils.isImagen(previewfiles[0])){
                img =  (RenderedImage)JAI.create("fileload", previewfiles[0].getPath());
                if(img != null){
                    ria = new RenderedImageAdapter(img);
                    if(ria != null){
                        bi = ria.getAsBufferedImage();
                        if(bi != null){
                            width= bi.getWidth();
                            height = bi.getHeight();
                            bi.flush();
                        }
                    }
                }
                
                for(int i=1; i<previewfiles.length ; i++){
                    img =  (RenderedImage)JAI.create("fileload", previewfiles[i].getPath());
                    if(img != null){
                        ria = new RenderedImageAdapter(img);
                        if(ria != null){
                            bi = ria.getAsBufferedImage();
                            if(bi != null){
                                if(bi.getWidth() != width || bi.getHeight() != height){
                                    date.setText(java.util.ResourceBundle.getBundle("lang").getString("distinct_size"));
                                    bi.flush();
                                    return;
                                }
                                bi.flush();
                            }else{
                                date.setText(java.util.ResourceBundle.getBundle("lang").getString("distinct_size"));
                                bi.flush();
                                return;
                            }
                        }else{
                            date.setText(java.util.ResourceBundle.getBundle("lang").getString("distinct_size"));
                            bi.flush();
                            return;
                        }
                        
                    }else{
                        date.setText(java.util.ResourceBundle.getBundle("lang").getString("distinct_size"));
                        bi.flush();
                        return;
                    }
                }
                date.setText(java.util.ResourceBundle.getBundle("lang").getString("size")+width+" * "+height+" px");
                
            }else{
                date.setText(java.util.ResourceBundle.getBundle("lang").getString("distinct_size"));
                bi.flush();
                return;
            }
        }else{
            if(previewfile == null)
                return;
            RenderedImage img;
            
            if(Utils.isImagen(previewfile)){
                img =  (RenderedImage)JAI.create("fileload", previewfile.getPath());
                if(img != null){
                    RenderedImageAdapter ria = new RenderedImageAdapter(img);
                    if(ria != null){
                        BufferedImage bi = ria.getAsBufferedImage();
                        if(bi != null){
                            icone.setIcon(new ImageIcon(scale(bi, 150)));
                            imagensize.setText(java.util.ResourceBundle.getBundle("lang").getString("size") + ": "+bi.getWidth()+" * "+bi.getHeight()+" px");
                            bi.flush();
                        }
                    }
                    
                }
            }
            previewfile = null;
        }
        System.gc();
    }
    
    private void clear(){
        icone.setIcon(null);
        nom.setText("");
        description.setText("");
        description.setIcon(null);
        taille.setText("");
        date.setText("");
        imagensize.setText("");
    }
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        nom = new javax.swing.JLabel();
        description = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        taille = new javax.swing.JLabel();
        jPanelPreview = new javax.swing.JPanel();
        icone = new javax.swing.JLabel();
        imagensize = new javax.swing.JLabel();
        preview = new javax.swing.JCheckBox();
        verif = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createTitledBorder(java.util.ResourceBundle.getBundle("lang").getString("detail")));
        nom.setText(java.util.ResourceBundle.getBundle("lang").getString("name"));

        description.setText(java.util.ResourceBundle.getBundle("lang").getString("description"));

        date.setText(java.util.ResourceBundle.getBundle("lang").getString("last_modification"));

        taille.setText(java.util.ResourceBundle.getBundle("lang").getString("file_size"));

        imagensize.setText(java.util.ResourceBundle.getBundle("lang").getString("image_size"));

        org.jdesktop.layout.GroupLayout jPanelPreviewLayout = new org.jdesktop.layout.GroupLayout(jPanelPreview);
        jPanelPreview.setLayout(jPanelPreviewLayout);
        jPanelPreviewLayout.setHorizontalGroup(
            jPanelPreviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanelPreviewLayout.createSequentialGroup()
                .add(imagensize, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                .addContainerGap())
            .add(icone, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
        );
        jPanelPreviewLayout.setVerticalGroup(
            jPanelPreviewLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelPreviewLayout.createSequentialGroup()
                .add(icone, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(imagensize)
                .add(20, 20, 20))
        );

        preview.setSelected(true);
        preview.setText(java.util.ResourceBundle.getBundle("lang").getString("preview"));
        preview.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        preview.setMargin(new java.awt.Insets(0, 0, 0, 0));
        preview.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                previewMouseReleased(evt);
            }
        });

        verif.setText(java.util.ResourceBundle.getBundle("lang").getString("verif_size"));
        verif.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        verif.setMargin(new java.awt.Insets(0, 0, 0, 0));
        verif.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                verifMouseReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelPreview, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .add(preview)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(verif)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(nom, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(description, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(taille, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .add(date, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanelPreview, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(preview)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(verif)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(nom)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(description)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(taille)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(date)
                .addContainerGap(25, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void verifMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_verifMouseReleased
        if(verif.isSelected()){
            File [] files = chooser.getSelectedFiles();
            if(files.length > 1){
                previewfiles = files;
                run_multiple_files = true;
                mythread = new Thread(this);
                mythread.start();
            }
        }
    }//GEN-LAST:event_verifMouseReleased
    
    private void previewMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previewMouseReleased
        if(preview.isSelected()){
            if(icone.getIcon() == null)
                setDetails(chooser.getSelectedFile());
            jPanelPreview.setVisible(true);
        }else{
            jPanelPreview.setVisible(false);
        }
    }//GEN-LAST:event_previewMouseReleased
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel date;
    private javax.swing.JLabel description;
    private javax.swing.JLabel icone;
    private javax.swing.JLabel imagensize;
    private javax.swing.JPanel jPanelPreview;
    private javax.swing.JLabel nom;
    private javax.swing.JCheckBox preview;
    private javax.swing.JLabel taille;
    private javax.swing.JCheckBox verif;
    // End of variables declaration//GEN-END:variables
    
}
