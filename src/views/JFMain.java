/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import models.Graph;
import models.Node;

/**
 *
 * @author ACWIN7C140
 */
public class JFMain extends JFrame{
    
    public static int time=10;
    private Graph<String> g;
    public JFMain(){
        super();
        CGraph canvas = new CGraph();
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        JButton jb = new JButton("Nodo");
        jb.setAlignmentX((float) 0);
        
        JSlider jSlider = new JSlider();
        jSlider.setOpaque(false);
        jSlider.setMaximum(20);
        jSlider.setValue(canvas.startTimerDelay);
        JLabel jlv=new JLabel(canvas.startTimerDelay+"");
        jSlider.addChangeListener((ChangeEvent e) -> {
            canvas.setSimDelay(jSlider.getValue());
            jlv.setText(""+jSlider.getValue());
        });
        
        JButton jba=new JButton("Arista");
        
        JButton jbReset = new JButton("Reset");
        
        JButton jbDijkstra = new JButton("Dijkstra");
        
        jbReset.addActionListener((ActionEven)->{
            canvas.reset();
            canvas.clearPath();
        });
        
        jba.addActionListener((ActionEvent e) -> {
            System.out.println("----------"+canvas.edgeMode);
            JDEdge jde = new JDEdge(false);
            jde.addAcceptActionListener((ActionEvent e1) -> {
                canvas.setCurrentDist(jde.getEdgeSize());
//                canvas.dispelMess();
                canvas.paintMessage("Seleccione el vertice 1", false);
                jba.setText("Cancelar");
                canvas.edgeMode();
                jb.setEnabled(false);
                jbReset.setEnabled(false);
                jbDijkstra.setEnabled(false);
                canvas.clearPath();
            });
            if(jba.getText().equals("Arista")){
                jde.setVisible(true);
            }else{
                canvas.cancelEdgeMode();
                jba.setText("Arista");
                jb.setEnabled(true);
                jbReset.setEnabled(true);
            }
        });
        
        canvas.addConfirmEgdeEv((ActionEvent)->{//Se confirma un arista
            jba.setText("Arista");
            canvas.dispelMess();
            jb.setEnabled(true);
            jbReset.setEnabled(true);
            jbDijkstra.setEnabled(true);
        });
        
        canvas.confirmEvent((ActionEvent)->{//Cuando se confirma un nodo
            jb.setText("Nodo");
            jba.setEnabled(true);
            jbReset.setEnabled(true);
            jbDijkstra.setEnabled(true);
        });
        
        jb.addActionListener((ActionEvent)->{
            if(canvas.grab){
                canvas.desacDrag();
                jb.setText("Nodo");
                jba.setEnabled(true);
                jbReset.setEnabled(true);
                jbDijkstra.setEnabled(true);
            }else{
                JDAddNode jda = new JDAddNode(false);
                jda.addAcceptActionListener((ActionEvent e)->{
                    if(canvas.getNodes().contains(jda.getNode())){
                        int opc = JOptionPane.showConfirmDialog(rootPane, "Nombre repetido. ¿Continuar?");
                        if(opc==JOptionPane.OK_OPTION){
                            canvas.clearPath();
                            canvas.grab(jda.getNode().getInfo().toString());
                            jb.setText("Cancelar");
                            jba.setEnabled(false);
                            jbReset.setEnabled(false);
                            jbDijkstra.setEnabled(false);
                        }else{
                            canvas.desacDrag();
                            jb.setText("Nodo");
                            jba.setEnabled(true);
                            jbReset.setEnabled(true);
                            jbDijkstra.setEnabled(true);
                        }
                    }else{
                        jb.setText("Cancelar");
                        jba.setEnabled(false);
                        jbReset.setEnabled(false);
                        jbDijkstra.setEnabled(false);
                        canvas.clearPath(); 
                        canvas.grab(jda.getNode().getInfo().toString());
                    }
                });
                jda.setVisible(true);
            }
        });
        
        jbDijkstra.addActionListener((ActionEvent e) -> {
            if(jbDijkstra.getText().equals("Dijkstra")){
                canvas.dijMode();
                canvas.paintMessage("Seleccione el vertice 1", false);
                jbDijkstra.setText("Cancel");
                jb.setEnabled(false);
                jba.setEnabled(false);
                jbReset.setEnabled(false);
                canvas.clearPath();
                Graph<String> g = new Graph<>();
                g.setN(canvas.nodes);
            }else{
                canvas.dispelMess();
                canvas.dijMode=false;
                canvas.dijModeSelected=false;
                jb.setEnabled(true);
                jba.setEnabled(true);
                jbReset.setEnabled(true);
                jbDijkstra.setText("Dijkstra");
            }
        });
        
        canvas.setDijkstraListener((ActionEvent e)->{
            jb.setEnabled(true);
            jba.setEnabled(true);
            jbReset.setEnabled(true);
            jbDijkstra.setText("Dijkstra");
            Node[] nodes = (Node[]) e.getSource();
            Node<String> a = nodes[0];
            Node<String> b = nodes[1];
            g = new Graph<>();
            g.setN(canvas.nodes);
            //hacer dijkstra g,a,b
            g.printGraph();
            canvas.path=g.dijkstra(a.getInfo(), b.getInfo());
            //dibujar el camino:
//            canvas.path=new ArrayList<>();
//            canvas.path.add("A");
//            canvas.path.add("B");
//            canvas.path.add("C");
            canvas.simMode(g.getSimLabels());
        });
        
        canvas.setPathListener((ActionEvent e) -> {
            double dist= g.getShortestPathDist();
            if(dist==Double.MAX_VALUE){
                canvas.paintMessage("Camino no econtrado", true);
            }else
                canvas.paintMessage("Distancia de camino "+dist, true);
        });
        
        canvas.setEditListener((ActionEvent e) -> {
            Node n = (Node) e.getSource();
            JDAddNode jda = new JDAddNode(n);
            jda.addAcceptActionListener((ActionEvent ev)->{
                canvas.editNode(n,jda.getNode().getInfo().toString());
            });
            jda.setVisible(true);
        });
        
        canvas.setEdEdgAL((ActionEvent e) -> {
            double[] n = (double[]) e.getSource();
            JDEdge jda = new JDEdge(n[0]);
            jda.addAcceptActionListener((ActionEvent ev)->{
                canvas.editEdge(n[1],jda.getEdgeSize());
            });
            jda.setVisible(true);
        });
        
        jp.setLayout(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.gridy=0;
        jp.add(jb,gbc);
        gbc.gridy++;
        jp.add(jba,gbc);
        gbc.gridy++;
        jp.add(new JLabel("Animation delay"),gbc);
        gbc.gridy++;
        jp.add(jSlider,gbc);
        gbc.gridy++;
        jp.add(jlv,gbc);
        gbc.gridy++;
        jp.add(jbDijkstra,gbc);
        gbc.gridy++;
        jp.add(jbReset,gbc);
        
        jp.setBackground(new Color(170,170,170));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(canvas,BorderLayout.CENTER);
        getContentPane().add(jp,BorderLayout.WEST);
        
        Action nodeAction =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jb.doClick();
            }
        };
        Action edgeAction =  new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jba.doClick();
            }
        };
        this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke('a'), "Node");
        this.getRootPane().getActionMap().put("Node",nodeAction);
        this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke('s'), "Edge");
        this.getRootPane().getActionMap().put("Edge",edgeAction);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setTitle("Dijkstra interactivo");
//        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
//        pack();
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new JFMain();
    }
}