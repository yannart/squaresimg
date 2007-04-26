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

package calc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author Yann Arthur Nicolas
 */
public class GenerateImage implements Runnable{
    
    private BufferedImage imagenprincipal;
    private ArrayList <BufferedImage> imagenescuadros;
    private int tipo_color;
    private int tipo_reparticion;
    private int progress;
    private int parametros;
    private BufferedImage salida;
    private Thread mythread;
    
    /** Creates a new instance of GaneraImagen */
    public GenerateImage(BufferedImage imagenprincipal, ArrayList <BufferedImage> imagenescuadros, int tipo_color, int tipo_reparticion, int parametros) {
        this.imagenprincipal = imagenprincipal;
        this.imagenescuadros = imagenescuadros;
        this.tipo_color = tipo_color;
        this.tipo_reparticion = tipo_reparticion;
        this.parametros = parametros;
    }
    
    public int isValid(){ //verifica si las imagenes son validas
        int error = 0;
        
        if(imagenprincipal== null){
            error = 1;
        }else{
            if(imagenescuadros.size() <= 0){
                error = 2;
            }else{
                int width = imagenescuadros.get(0).getWidth();
                int height = imagenescuadros.get(0).getHeight();
                for(int i=1; i<imagenescuadros.size() ; i++){
                    if(imagenescuadros.get(i).getWidth() != width || imagenescuadros.get(i).getHeight() != height)
                        error = 3;
                }
            }
        }
        
        return error;
    }
    
    public void calcula(){
        mythread = new Thread(this);
        mythread.start();
    }
    
    public void stopCalcula(){
        mythread = null;
    }
    
    public void run(){
        int width_big = imagenprincipal.getWidth();
        int height_big = imagenprincipal.getHeight();
        int width_small = imagenescuadros.get(0).getWidth();
        int height_small = imagenescuadros.get(0).getHeight();
        int num_imagenes = imagenescuadros.size();
        float num_pixel = width_small * height_small; //number of pixel of textures
        float num_pixel_right = (width_big % width_small)*height_small; //number of pixels when last horizontal texture
        float num_pixel_down = (height_big % height_small)*width_small; //number of pixels when last vertical texture
        float num_pixel_last = (width_big % width_small)*(height_big % height_small); //number of pixels when last texture
        
        int num_tot_cuadros = (int)Math.floor((height_big/height_small) * (width_big/width_small));
        if( num_tot_cuadros == 0) num_tot_cuadros = 1;
        int current_cuad = 0;
        int R;
        int G;
        int B;
        int X;
        int Y;
        int rgb;
        Random random = new Random();
        int prom_color;
        int R_prom;
        int G_prom;
        int B_prom;
        int cuad = 0;
        boolean lim_h = false;
        boolean lim_v = false;
        BufferedImage cuadro;
        try {
            salida = new BufferedImage(width_big,height_big, imagenprincipal.getType());
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("lang").getString("error_memory"), java.util.ResourceBundle.getBundle("lang").getString("error"), JOptionPane.ERROR_MESSAGE);
            mythread = null;
        }
        
        for(int i=0; i<height_big ; i+= height_small){ //para cada linea de cuadros
            if(tipo_reparticion == 1){ //horizontal aleatorio
                current_cuad = random.nextInt(num_imagenes);
            }
            
            for(int j=0; j<width_big ; j+= width_small){ //para cada columna de cuadros
                if(tipo_reparticion == 0){ //todo aleatorio
                    current_cuad = random.nextInt(num_imagenes);
                }
                
                if (mythread == null)
                    return;
                
                cuad++;
                long tot_R = 0;
                long tot_G = 0;
                long tot_B = 0;
                for(int k=0; k<height_small ; k++){//para cada linea de pixels
                    lim_v = false;
                    Y = k + i;
                    if(Y < height_big){
                        for(int l=0; l<width_small; l++){//para cada columna de pixels
                            lim_h = false;
                            X = l + j;
                            if(X < width_big){
                                rgb = imagenprincipal.getRGB(X, Y);
                                tot_R += (rgb >>16 ) & 0xFF;
                                tot_G += (rgb >>8 ) & 0xFF;
                                tot_B += rgb & 0xFF;
                            }else{
                                lim_h = true;
                            }
                        }
                    }else{
                        lim_v = true;
                    }
                    
                }
                if(!lim_v){ //no esta fuera del limite vertical
                    if(!lim_h){ //no esta fuera de nigun limite
                        prom_color = ((int)(tot_R/num_pixel)<<16)+((int)(tot_G/num_pixel)<<8)+(int)(tot_B/num_pixel);
                    }else{//esta fuera del limite horizontal
                        prom_color = ((int)(tot_R/num_pixel_right)<<16)+((int)(tot_G/num_pixel_right)<<8)+(int)(tot_B/num_pixel_right);
                    }
                }else{
                    if(!lim_h){ //esta fuera del limite vertical
                        prom_color = ((int)(tot_R/num_pixel_down)<<16)+((int)(tot_G/num_pixel_down)<<8)+(int)(tot_B/num_pixel_down);
                    }else{//esta fuera de los dos limites
                        prom_color = ((int)(tot_R/num_pixel_last)<<16)+((int)(tot_G/num_pixel_last)<<8)+(int)(tot_B/num_pixel_last);
                    }
                }
                
                R_prom = (prom_color >>16 ) & 0xFF;
                G_prom = (prom_color >> 8 ) & 0xFF;
                B_prom = prom_color & 0xFF;
                
                cuadro = imagenescuadros.get(current_cuad);
                
                for(int k=0; k<height_small ; k++){//para cada linea de pixels
                    Y = k + i;
                    if(Y < height_big){
                        for(int l=0; l<width_small; l++){//para cada columna de pixels
                            X = l + j;
                            if(X < width_big){
                                rgb = cuadro.getRGB( l, k);
                                R = (rgb >>16 ) & 0xFF;
                                G = (rgb >> 8 ) & 0xFF;
                                B = rgb & 0xFF;
                                
                                if(tipo_color == 1){// promedio de colores
                                    salida.setRGB(X, Y,((R + R_prom)/2<<16)+(((G + G_prom)/2)<<8)+(((B + B_prom)/2)));
                                }else if(tipo_color == 0){ // multiplicacion de colores
                                    salida.setRGB(X, Y,((int)(R*R_prom/255.0)<<16)+((int)(G*G_prom/255.0)<<8)+((int)(B*B_prom/255.0)));
                                }else if(tipo_color == 2){//dar tono
                                    if(parametros == 0){ //luminancia
                                        R =  (int)(0.3 * (float)R + 0.59 * (float)G + 0.11 * (float)B);
                                    } else{
                                        int max = R;
                                        if(G > R) max = G;
                                        if(B > max) max = B;
                                        
                                        if(parametros == 1){ //desaturacion
                                            int min = R;
                                            if(G < R) min = G;
                                            if(B < min) min = B;
                                            R =  (max + min) / 2;
                                            
                                        } else{ //maximo
                                            R =  max;
                                        }
                                    }
                                    salida.setRGB(X, Y,((int)(R*R_prom/255.0)<<16)+((int)(R*G_prom/255.0)<<8)+((int)(R*B_prom/255.0)));
                                    
                                }else if(tipo_color == 3){//diferencia
                                    salida.setRGB(X, Y,((int)((255.0-R)*R_prom/255.0)<<16)+((int)((255.0-G)*G_prom/255.0)<<8)+((int)((255.0-B)*B_prom/255.0)));
                                }else{
                                    //cuadratico
                                    salida.setRGB(X, Y,((int)(Math.sqrt(R * R_prom))<<16)+((int)(Math.sqrt(G * G_prom))<<8)+((int)(Math.sqrt(B * B_prom))));
                                }
                            }
                        }
                    }
                }
                
                current_cuad++;
                if(current_cuad == num_imagenes)
                    current_cuad = 0;
                
                progress = (cuad * 100)/num_tot_cuadros;
            }
        }
        
        progress = 1000;
    }
    
    public synchronized int getProgress(){
        return progress;
    }
    
    public BufferedImage getImagenFinal(){
        return salida;
    }
    
}
