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

import gui.Frame;
import java.awt.image.BufferedImage;
import javax.swing.JFileChooser;
import javax.media.jai.*;
import java.awt.image.RenderedImage;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Yann Arthur Nicolas
 */
public class ImageFileChooser extends JFileChooser{
    
    private Frame frame;
    private boolean cuadros=false;
    
    public ImageFileChooser(Frame frame) {
        super();
        this.frame = frame;
        this.setMultiSelectionEnabled(false);
        this.setFileFilters();
        this.setAccessory(new FilesPreview(this));
    }
    
    public void approveSelection(){
        RenderedImage img;
        File [] files;
        if(isCuadros()){
            files = this.getSelectedFiles();
        }else{
            files = new File[1];
            files[0] = this.getSelectedFile();
        }
        
        for(int i=0; i<files.length; i++){
            if(!Utils.isImagen(files[i])){
                JOptionPane.showMessageDialog(this, files[i].getName() + java.util.ResourceBundle.getBundle("lang").getString("incompatible_image"), java.util.ResourceBundle.getBundle("lang").getString("invalid_image"), JOptionPane.ERROR_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/images/error.png")));
            }else{
                img =  (RenderedImage)JAI.create("fileload", files[i].getPath());
                RenderedImageAdapter ria = new RenderedImageAdapter(img);
                BufferedImage bi = ria.getAsBufferedImage();
                
                if(isCuadros())
                    frame.addImagenCuadro(bi);
                else
                    frame.setImagenPrincipal(bi);
            }
        }
        this.cancelSelection();
    }
    
    private void setFileFilters(){
        
        this.setFileFilter(null);
        for(int i=0; i<Utils.exts.length; i++){
            final int j = i;
            FileFilter filefilter = new FileFilter(){
                public boolean accept(File f) {
                    if (f.isDirectory()) return true;
                    return Utils.isExt(f, Utils.exts[j]);
                }
                
                public String getDescription() {
                    return java.util.ResourceBundle.getBundle("lang").getString("images") + Utils.exts[j];
                }
            };
            
            this.addChoosableFileFilter(filefilter);
        }
        
        FileFilter filefilter = new FileFilter(){
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                return Utils.isImagen(f);
            }
            
            public String getDescription() {
                return java.util.ResourceBundle.getBundle("lang").getString("all_images");
            }
        };
        
        this.addChoosableFileFilter(filefilter);
    }
    
    public void setCuadros(boolean cuadros) {
        this.cuadros = cuadros;
        setSelectedFile(null);
        setSelectedFiles(null);
        setMultiSelectionEnabled(cuadros);
    }

    public boolean isCuadros() {
        return cuadros;
    }
    
}
