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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;

/**
 *
 * @author Yann Arthur Nicolas
 */

public class ImageCuadrosCellRenderer extends JPanel implements ListCellRenderer {
    
    private BufferedImage image;
    private boolean selected;
    private int imageWidth;
    private int imageHeight;
    /** Creates a new instance of ImageCuadrosPanel */
    public ImageCuadrosCellRenderer() {
        this.setLayout(new BorderLayout());
    }
    
    public void setImage(BufferedImage image) {
        this.image = image;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        Dimension dimension= new Dimension(imageWidth + 10, imageHeight + 10);
        setPreferredSize(dimension);
        setSize(dimension);
    }
    
    public Component getListCellRendererComponent(
            JList list,
            Object value,            // value to display
            int index,               // cell index
            boolean isSelected,      // is the cell selected
            boolean cellHasFocus)    // the list and the cell have the focus
    {
        setImage((BufferedImage)list.getModel().getElementAt(index));
        setBackground(list.getBackground());
        setToolTipText(java.util.ResourceBundle.getBundle("lang").getString("size")+ imageWidth + " * " + imageHeight);
        setEnabled(true);
        setOpaque(true);
        this.selected = isSelected;
        return this;
    }
    
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image != null)
            g.drawImage(image, 5, 5, imageWidth, imageHeight, this);
        
        
        if(selected){
            g.setColor(new Color(200,200,255,150));
            g.fillRect(5, 5, imageWidth, imageHeight);
            g.setColor(new Color(0,0,0));
            g.drawRect(4, 4, imageWidth + 1, imageHeight + 1);
        }
    }
    
}
