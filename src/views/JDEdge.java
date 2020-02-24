/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author León
 */
public class JDEdge extends JDialog{
    
    GridBagConstraints gbc;
    JTextField jtfName;
    private final JButton jba;
    private final JButton jbc;
    
    public JDEdge(double dist){
        this(true);
        jtfName.setText(String.valueOf(dist));
    }
    
    public JDEdge(boolean edit){
        gbc=new GridBagConstraints();
        JPanel jp = new JPanel();
        getContentPane().setLayout(new BorderLayout());
        jp.setLayout(new GridBagLayout());
        setSize(200, 200);
        setLocationRelativeTo(null);
        setTitle(edit?"Edicion de arista":"Ingreso de arista.");
        //gbc.anchor=GridBagConstraints.CENTER;
        gbc.weightx=1;
        gbc.gridy=0;
        gbc.gridx=0;
        jp.add(new JLabel("Tamaño:"),gbc);
        gbc.gridx++;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        jtfName=new JTextField();
        jtfName.addKeyListener(new keyListenerNumber());
        jtfName.setColumns(20);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jp.add(jtfName,gbc);
        getContentPane().add(jp,BorderLayout.CENTER);
        
        JPanel jpB = new JPanel();
        jba = new JButton("Aceptar");
        jpB.add(jba);
        jbc=new JButton("Cancelar");
        jbc.addActionListener((ActionEvent)->{dispose();});
        jpB.add(jbc);
        getRootPane().setDefaultButton(jba);
        setModal(true);
        getContentPane().add(jpB,BorderLayout.SOUTH);
    }
    
    public void addAcceptActionListener(ActionListener al){
        jba.addActionListener((ActionEvent e) -> {
            double d = getEdgeSize();
            if(d>-1){
                dispose();
                al.actionPerformed(e);
            }else{
                JOptionPane.showMessageDialog(this, "Datos erroneos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    public double getEdgeSize(){
        double size;
        try {
            size = Double.parseDouble(jtfName.getText());
        } catch (Exception e) {
            return -1;
        }
        return size;
    }
    
    public void addCancelAcion(ActionListener al){
        jbc.addActionListener(al);
    }
}
