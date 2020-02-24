/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;

/**
 *
 * @author León
 */
public class DoubleBuffer extends Panel {

    private int bufferWidth;
    private int bufferHeight;
    private Image bufferImage;
    private Graphics bufferGraphics;

    public void paint(Graphics g) {
    //    checks the buffersize with the current panelsize
        //    or initialises the image with the first paint
        if (bufferWidth != getSize().width
                || bufferHeight != getSize().height
                || bufferImage == null || bufferGraphics == null) {
            resetBuffer();
        }
        if (bufferGraphics != null) {
            //this clears the offscreen image, not the onscreen one
            bufferGraphics.clearRect(0, 0, bufferWidth, bufferHeight);

        //calls the paintbuffer method with
            //the offscreen graphics as a param
            paintBuffer(bufferGraphics);

            //we finaly paint the offscreen image onto the onscreen image
            g.drawImage(bufferImage, 0, 0, this);
        }
    }
    
    public void update(Graphics g){
       paint(g);
    }

    private void resetBuffer() {
        // always keep track of the image size
        bufferWidth = getSize().width;
        bufferHeight = getSize().height;

        //    clean up the previous image
        if (bufferGraphics != null) {
            bufferGraphics.dispose();
            bufferGraphics = null;
        }
        if (bufferImage != null) {
            bufferImage.flush();
            bufferImage = null;
        }
        System.gc();

        //    create the new image with the size of the panel
        bufferImage = createImage(bufferWidth, bufferHeight);
        bufferGraphics = bufferImage.getGraphics();
    }

    public void paintBuffer(Graphics g) {

    }
}
