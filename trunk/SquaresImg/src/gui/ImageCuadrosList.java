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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JList;

/**
 *
 * @author Yann Arthur Nicolas
 */
public class ImageCuadrosList extends JList{
    
    ImageCuadrosListModel listmodel;
    
    public ImageCuadrosList() {
        listmodel = new ImageCuadrosListModel();
        this.setCellRenderer(new ImageCuadrosCellRenderer());
        this.setModel(listmodel);
        this.setToolTipText(java.util.ResourceBundle.getBundle("lang").getString("squares_images"));
        this.setBackground(new Color(241,241,241));
        
    }
    
    public void add(BufferedImage bi){
        listmodel.add(bi);
    }
    
    public void remove(int index){
        listmodel.remove(index);
        System.gc ();
    }
    
    public void clear(){
        listmodel.clear();
        System.gc ();
    }
    
    public void removeSelected(){
        int [] selected = this.getSelectedIndices();
        for(int i= selected.length -1 ; i>= 0 ; i--){
            remove(selected[i]);
        }
        System.gc ();
    }
    
    public ArrayList <BufferedImage> getData(){
        return listmodel.getData();
    }
}
