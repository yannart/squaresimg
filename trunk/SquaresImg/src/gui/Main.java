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

import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Yann Arthur Nicolas
 */
public class Main {
    
    static Frame frame;
    
    public Main() {
    }
    
    public static void main(String args[]) {
        final Main main = new Main();
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame = new Frame();
                Start start = new Start(main);
                /** Lecture de la taille de l'écran */
                java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                start.pack();
                start.setLocation((screenSize.width-start.getWidth())/2,(screenSize.height-start.getHeight())/2);
                start.setVisible(true);
                frame.pack();
                /* la fenêtre prend sa taille... */
                frame.setLocation((screenSize.width-frame.getWidth())/2,(screenSize.height-frame.getHeight())/2);
            }
        });
    }
    
    public void viewFrame(){
        frame.setVisible(true);
    }
}
