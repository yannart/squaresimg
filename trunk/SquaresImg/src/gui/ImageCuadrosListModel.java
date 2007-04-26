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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author Yann Arthur Nicolas
 */
public class ImageCuadrosListModel extends AbstractListModel{
    
    private ArrayList <BufferedImage> list;
    
    public ImageCuadrosListModel() {
        list = new ArrayList();
    }
    
    public int getSize() {
        return list.size();
    }
    
    public Object getElementAt(int index) {
        return list.get(index);
    }
    
    public void add(BufferedImage bi){
        list.add(bi);
        this.fireContentsChanged(this, 0, getSize() - 1);
    }
    
    public void remove(int index){
        list.remove(index);
        fireIntervalRemoved(this, index, index);
    }
    
    public void clear(){
        list.clear();
        this.fireContentsChanged(this, 0, getSize() - 1);
    }
    
    public ArrayList <BufferedImage> getData(){     
        return list;
    }
    
}
