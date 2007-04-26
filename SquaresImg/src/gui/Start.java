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
import javax.swing.JLabel;
import javax.swing.JWindow;

/**
 *
 * @author Yann Arthur Nicolas
 */
public class Start extends JWindow implements Runnable{
    
    Thread mythread;
    Main parent;
    
    public Start(Main parent) {
        this.setSize(300,150);
        this.setLayout(new BorderLayout());
        this.parent = parent;
        JLabel imagen = new JLabel();
        imagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/start.png")));
        this.add(imagen);
        mythread = new Thread(this);
    }
    
    public void run(){
        try {
            mythread.sleep(5000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        parent.viewFrame();
        this.dispose();
    }
    
    public void setVisible(boolean visible){
        super.setVisible(visible);
        mythread.start();
    }
            
    
}
