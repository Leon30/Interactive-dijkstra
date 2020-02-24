/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import views.balloontip.BalloonTip;
import views.balloontip.styles.RoundedBalloonStyle;

/**
 *
 * @author León
 */
public class keyListenerNumber implements KeyListener{
    @Override
    public void keyTyped(KeyEvent e) {
        int a = (int)e.getKeyChar();
        if(a!= 46 && a>31 && a!=127 && (a<48 || a>57)){
                BalloonTip balloonTip = new BalloonTip((JComponent) e.getSource(), "Ingrese un numero", new RoundedBalloonStyle(10, 10, Color.WHITE, Color.BLACK), false);
                balloonTip.setVisible(true);
                ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
                exec.schedule(new Runnable() {
                          public void run() {
                                  balloonTip.setVisible(false);
                          }
                     }, 2, TimeUnit.SECONDS);
                e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
