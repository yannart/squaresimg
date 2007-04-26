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

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codec.TIFFEncodeParam;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Yann Arthur Nicolas
 */
public class ImageFileSave extends JFileChooser{
    
    private BufferedImage bi = null;
    
    public ImageFileSave(BufferedImage bi) {
        this.bi = bi;
        this.setFileFilters();
    }
    
    
    
    public void approveSelection(){
        RenderedImage img;
        File file;
        file = this.getSelectedFile();
        
        FileOutputStream out = null;
        
        if(Utils.isExt(file, Utils.jpeg)|| Utils.isExt(file, Utils.jpg)){
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            JPEGEncodeParam params = new JPEGEncodeParam();
            params.setQuality((float)1.0);
            ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG",out,params);
            if(encoder == null) {
                System.exit(0);
            }
            try {
                encoder.encode(bi);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.cancelSelection();
        }else if(Utils.isExt(file, Utils.tif)|| Utils.isExt(file, Utils.tiff)){
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            TIFFEncodeParam params = new TIFFEncodeParam();
            params.setCompression(TIFFEncodeParam.COMPRESSION_NONE);
            ImageEncoder encoder = ImageCodec.createImageEncoder("TIFF",out,params);
            if(encoder == null) {
                System.exit(0);
            }
            try {
                encoder.encode(bi);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.cancelSelection();
        }else if(Utils.isExt(file, Utils.bmp)){
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            ImageEncoder encoder = ImageCodec.createImageEncoder("BMP",out,null);
            if(encoder == null) {
                System.exit(0);
            }
            try {
                encoder.encode(bi);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.cancelSelection();
        }else{
            JOptionPane.showMessageDialog(this, file.getName() + java.util.ResourceBundle.getBundle("lang").getString("incompatible_image") +
                    java.util.ResourceBundle.getBundle("lang").getString("dont_forget_extention") +
                    java.util.ResourceBundle.getBundle("lang").getString("example"), java.util.ResourceBundle.getBundle("lang").getString("invalid_image"), JOptionPane.ERROR_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("/images/error.png")));
        }
        
    }
    
    private void setFileFilters(){
        
        FileFilter filefilter = new FileFilter(){
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                return Utils.isImagenSave(f);
            }
            
            public String getDescription() {
                return java.util.ResourceBundle.getBundle("lang").getString("all_images_format");
            }
        };
        
        this.setFileFilter(filefilter);
    }
    
}

