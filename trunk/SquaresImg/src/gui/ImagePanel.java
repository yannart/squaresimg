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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Yann Arthur Nicolas
 */
public class ImagePanel extends JPanel{
    
    private BufferedImage image = null;
    private double scale;
    //private Dimension inicialDimension;
    private double imageWidth;
    private double imageHeight;
    private JScrollPane parent;
    
    public ImagePanel(JScrollPane parent) {
        this.parent = parent;
    }
    
    public BufferedImage getImage() {
        return image;
    }
    
    public int setImage(BufferedImage image) {
        this.image = image;
        this.setToolTipText(java.util.ResourceBundle.getBundle("lang").getString("size")+image.getWidth()+" * "+image.getHeight());
        System.gc();
        return zoomAjuste();
    }
    
    public int zoomIn(){
        if(image == null)
            return 0;
        scale *= 2.0;
        
        imageWidth = image.getWidth()*scale;
        imageHeight = image.getHeight()*scale;
        
        Dimension dimension= new Dimension((int)imageWidth, (int)imageHeight);
        setPreferredSize(dimension);
        setSize(dimension);
        repaint();
        return (int) (scale * 100.0);
    }
    
    public int zoomOut(){
        if(image == null)
            return 0;
        scale /= 2.0;
        
        imageWidth = image.getWidth()*scale;
        imageHeight = image.getHeight()*scale;
        
        Dimension dimension= new Dimension((int)imageWidth, (int)imageHeight);
        setPreferredSize(dimension);
        setSize(dimension);
        repaint();
        return (int) (scale * 100.0);
    }
    
    public int zoom100(){
        if(image == null)
            return 0;
        scale = 1;
        
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        
        Dimension dimension= new Dimension((int)imageWidth, (int)imageHeight);
        setPreferredSize(dimension);
        setSize(dimension);
        repaint();
        return 100;
    }
    
    public int zoomAjuste(){
        if(image == null)
            return 0;
        
        int w = parent.getBounds().width - 2;
        int h = parent.getBounds().height - 2;
        
        double scaleH = (double)w/(double)image.getWidth();
        double scaleV = (double)h/(double)image.getHeight();    
        
        scale = scaleH > scaleV ? scaleV : scaleH;
        
        imageWidth = image.getWidth()*scale;
        imageHeight = image.getHeight()*scale;
        
        Dimension dimension= new Dimension((int)imageWidth, (int)imageHeight);
        setPreferredSize(dimension);
        setSize(dimension);
        return (int) (scale * 100.0);
    }
    
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(image != null)
            g.drawImage(image, 0, 0, (int)imageWidth, (int)imageHeight, this);
    }
    
}
