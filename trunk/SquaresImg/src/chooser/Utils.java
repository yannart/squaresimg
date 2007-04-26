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

import java.io.File;

/**
 *
 * @author Yann Arthur Nicolas
 */
public class Utils {
    
    public final static String [] exts = {"jpeg","jpg","gif","tiff","tif","png","bmp"};
    public final static String [] extsave = {"jpeg","jpg","tiff","tif","bmp"};
    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";
    public final static String bmp = "bmp";
    
    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        
        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    public static boolean isImagen(File f){
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.tiff) ||
                    extension.equals(Utils.tif) ||
                    extension.equals(Utils.bmp) ||
                    extension.equals(Utils.gif) ||
                    extension.equals(Utils.jpeg) ||
                    extension.equals(Utils.jpg) ||
                    extension.equals(Utils.png)) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }
    
    public static boolean isImagenSave(File f){
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.tiff) ||
                    extension.equals(Utils.tif) ||
                    extension.equals(Utils.bmp) ||
                    extension.equals(Utils.jpeg) ||
                    extension.equals(Utils.jpg)) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }
    
    public static boolean isExt(File f, String ext){
        ext = ext.toLowerCase();
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(ext)) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }
}
