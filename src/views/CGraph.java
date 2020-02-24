/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import models.Edge;
import models.Label;
import models.Node;

/**
 *
 * @author ACWIN7C140
 */
public class CGraph extends DoubleBuffer implements MouseMotionListener,MouseListener{
    
//    public static final Color NODE_COLOR = new Color(7,16,19);
    public static final Color NODE_COLOR = new Color(117,16,19);
    public static final Color EDGE_TEXT_COLOR = new Color(157,92,99);
    public static final Color SELECTED_COLOR = new Color(117,177,122);
//    public static final Color SELECTED_COLOR = Color.WHITE;
    boolean grab=false;
    int grabbingX=-10;
    int grabbingY=-10;
    int startTimerDelay = 2;
    int nodeDiam = 40;
    public Timer timer;
    String lastName="";
    ArrayList<Point> nodePoints=new ArrayList<>();
    ArrayList<Point[]> edgesPoints = new ArrayList<>();//par de puntos de aristas
    ArrayList<Edge> edges = new ArrayList<>();
    ArrayList<Double> dists = new ArrayList<>();
    ArrayList<Node<String>> nodes = new ArrayList<>();
    ArrayList<Boolean> selected = new ArrayList<>();
    private Color fillColor= NODE_COLOR;
    private boolean flag=true;
    private ActionListener confirmNodeAL;
    boolean edgeMode=false;
    boolean edgeModeSelected=false;
    boolean dijMode=false;
    boolean dijModeSelected=false;
    private ActionListener alE;
    int nodeSelected;
    int dijSelected;
    private ActionListener delAL;
    private Color edgeColor=new Color(235,81,96);
    private double currentDist;
    ArrayList<String> path;
    
    private boolean edgeSelFlag = false;
    private ActionListener dijAL;
    private ActionListener edAL;
    private ActionListener ededgAL;
    private int overEdge=-1;
    private int wOverEd=20;
    
    private boolean help=false;
    private int helpAlpha=0;
    private long auxHelpTime=-1;
    private boolean helpDispel = false;
    private boolean autoDispel = true;
    private String helpMess = "";
    
    private int draggedNode = -1;
    private Point draggedDif;
    
    private boolean pathMode=false;
    private boolean simMode=false;
    private ArrayList<ArrayList<Label<String>>> labels;
    private ActionListener pathAL;
    private int currentLabel=0;
    private Timer simTimer;
    
    public CGraph(){
        simTimer=new Timer(startTimerDelay*1000, (ActionEvent e) -> {
            if(currentLabel<labels.size()-1) currentLabel++;
            else{
                pathMode=true;
                pathAL.actionPerformed(null);
                simTimer.stop();
            }
            repaint();
        });
        timer = new Timer(10, (ActionEvent e) -> {
            mouseOverNode();
            mouseOverEdge();
            repaint();
//            System.out.println("Grab: "+grab);
//            System.out.println("lastn: "+lastName);
//            System.out.println("list: "+Arrays.toString(listToString()));
            //if(grab) System.out.println("grab: "+grabbingX+", "+grabbingY);
        });
        timer.start();
        this.setBackground(new Color(223,224,226));
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }
    
    public void setSimDelay(int delay){
        simTimer.setDelay(delay*1000);
    }
    
    public void simMode(ArrayList<ArrayList<Label<String>>> labels){
        this.labels=labels;
        simMode=true;
        simTimer.start();
    }
    
    public void paintMessage(String s,boolean autoDispel){
        if(autoDispel) auxHelpTime=System.currentTimeMillis();
        this.helpAlpha=0;
        this.helpMess=s;
        this.autoDispel=autoDispel;
        this.helpDispel=false;
        help=true;
    }
    
    public void dispelMess(){
        if(help){
            helpDispel=true;
            autoDispel=true;
        }
    }
    
    public void edgeMode(){
        edgeMode=true;
    }
    
    public void cancelEdgeMode(){
        edgeMode=false;
        edgeModeSelected=false;
    }
    
    public void dijMode(){
        dijMode=true;
    }
    
    public void cancelDijMode(){
        dijMode=false;
        dijModeSelected=false;
    }
    
    public void addConfirmEgdeEv(ActionListener alE){
        this.alE=alE;
    }
    
    public void setEditListener(ActionListener al){
        this.edAL=al;
    }
    
    public String[] listToString(){
        String[] s = new String[nodePoints.size()];
        for (int i = 0; i < s.length; i++) {
            s[i]=((i<nodes.size())?nodes.get(i):lastName)+": "+nodePoints.get(i).x+" ,"+nodePoints.get(i).y+", s:"+selected.get(i);
        }
        return s;
    }
    
    public void grab(String name){//aceptar de jdadd
        grab=true;
        lastName=name;
        if(this.getMousePosition()!=null){
            this.grabbingX=this.getMousePosition().x;
            this.grabbingY=this.getMousePosition().y;
        }
    }
    
    public void saveNode(int x,int y){
        nodePoints.add(new Point(x, y));
        nodes.add(new Node(lastName));
        selected.add(false);
        repaint();
    }
    
    @Override
    public void paintBuffer(Graphics graphics){
        Graphics2D g = (Graphics2D) graphics;
//        System.out.println("painting " +System.currentTimeMillis());
        
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        
        int wi=(this.getSize().width);
        int he=(this.getSize().height);
        if(flag){
            Point ap = new Point(wi/2,he/2);
            nodePoints.add(ap);
            Node a=new Node("A");
            nodes.add(a);
            selected.add(false);
            
            Point bp=new Point(wi/2+170,he/2-170);
            nodePoints.add(bp);
            Node b = new Node("B");
            nodes.add(b);
            selected.add(false);
            
            Point cp=new Point(wi/2+170,he/2+170);
            nodePoints.add(cp);
            Node c = new Node("C");
            nodes.add(c);
            selected.add(false);
            flag=false;
            
            Edge<String> ea = a.linkDouble(b, 23);
            edges.add(ea);
            edgesPoints.add(new Point[]{ap,bp});
            dists.add(23d);
            
            Edge<String> eb = b.linkDouble(c, 15);
            edges.add(eb);
            edgesPoints.add(new Point[]{bp,cp});
            dists.add(15d);
        }
        if(edgeMode){
            //paint grabbed edge
            if(edgeModeSelected){
                g.setColor(edgeColor);
                Point pos =nodePoints.get(nodeSelected);
                Point point = getMousePosition();
                if(point != null){
                    g.setStroke(new BasicStroke(4));
                    g.drawLine(pos.x, pos.y, point.x, point.y);
                }
            }
        }
        //paint path
        if(pathMode && path!=null && path.size()>1){
            for (int i = 0; i < path.size()-1; i++) {
                String v1 = path.get(i);
                String v2 = path.get(i+1);
                Point p1 = searchNodePos(v1);
                Point p2 = searchNodePos(v2);
                g.setColor(Color.BLACK);
                g.setStroke(new BasicStroke(9));
                g.drawLine(p1.x,p1.y,p2.x,p2.y);
            }
        }
        
        //paint edges
        for (int i = 0; i < edgesPoints.size(); i++) {
            Point[] points = edgesPoints.get(i);
            g.setColor(edgeColor);
            g.setStroke(new BasicStroke(4));
            g.drawLine(points[0].x,points[0].y,points[1].x,points[1].y);
            Point p = midPoint(points[1],points[0]);
            g.setColor(Color.WHITE);
            g.drawString(dists.get(i)+"", p.x, p.y);
            g.setColor(EDGE_TEXT_COLOR);
            g.drawString(dists.get(i)+"", p.x+2, p.y+2);
        }
        Point mp = getMousePosition();
        if(draggedNode!=-1 && mp != null){
            Point p = nodePoints.get(draggedNode);
            p.x=mp.x-draggedDif.x;
            p.y=mp.y-draggedDif.y;
        }
        //paint nodes
        for (int i = 0; i < nodePoints.size(); i++) {
            Point point = nodePoints.get(i);
//            if(i<nodes.size()) System.out.println("----------mem paint: "+nodes.get(i));
//            else System.out.println("----------mem paint: "+lastName);
            if(selected.get(i) || (draggedNode==i && draggedNode != -1)){
                drawNode(g, point.x, point.y,SELECTED_COLOR);
            }else
                drawNode(g, point.x, point.y,NODE_COLOR);
            Node node = nodes.get(i);
            drawCenteredHString(g,node.toString(), point.x, point.y-nodeDiam/2-4);
        }
        //paint sim
        if(simMode){
            if(labels!=null && !labels.isEmpty()){
                int i = currentLabel;
                ArrayList<Label<String>> labelsSimulation = this.labels.get(i);
                for (Label<String> label : labelsSimulation) {
                    Node<String> n = label.getA();
                    Point p = searchNodePos(n.getInfo());
                    String labelText = "["+((label.getB()==null || label.getB().getInfo()==null)?"-":label.getB())+", "+(label.getDistance()==Double.MAX_VALUE?"inf":label.getDistance())+"] ("+label.getIteration()+")";
                    drawCenteredHString(g,labelText, p.x, p.y+nodeDiam/2+g.getFontMetrics().getMaxAscent());
                    if(label.isVisited()){
                        g.setColor(Color.RED);
                        Point a=new Point(p.x-nodeDiam/2, p.y-nodeDiam/2);
                        Point b = new Point(p.x+nodeDiam/2, p.y+nodeDiam/2);
                        g.setStroke(new BasicStroke(7));
                        g.drawLine(a.x, a.y, b.x, b.y);
                    }
                }
            }
        }
        //over edge circle
        if(overEdge>-1){
            Point[] points = edgesPoints.get(overEdge);
            Point p = midPoint(points[0],points[1]);
            g.setStroke(new BasicStroke(5));
            g.setColor(Color.RED);
            g.drawOval(p.x-wOverEd/2, p.y-wOverEd/2, wOverEd, wOverEd);
        }
        //paint grabbed node
        if(grab){
            drawNode(g, grabbingX, grabbingY,NODE_COLOR);
            drawCenteredHString(g,lastName, grabbingX, grabbingY-nodeDiam/2-4);
        }
        //paint help
        if(help){
            if(helpDispel){
                if(helpAlpha>5) helpAlpha-=5;
            }else{
                if(helpAlpha<170) helpAlpha+=5;
            }
            g.setColor(new Color(235, 207, 52, helpAlpha));
            int rwidth = 250;
            int rheight = 50;
            int xoff = 10;
            g.fillRoundRect(wi-rwidth-xoff, xoff, rwidth, rheight, 30,30);
            
            if(autoDispel && !helpDispel && (System.currentTimeMillis()-auxHelpTime)>5000){
                helpDispel=true;
            }
            if(helpAlpha<0){
                help=false;
            }
            
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
            drawCenteredVString(g, this.helpMess, wi-rwidth-xoff+20, xoff+rheight/2,helpAlpha);
        }
    }
    
    Point searchNodePos(String node){
        for (int i = 0; i < nodePoints.size(); i++) {
            if(nodes.get(i).getInfo().equals(node)){
                return nodePoints.get(i);
            }
        }
        return null;
    }
    
    public static Point midPoint(Point p1,Point p2) {
        double mx = (p1.x + p2.x)/2;
        double my = (p1.y + p2.y)/2;
        return new Point((int)mx,(int)my);
    }
    
    public void drawCenteredHString(Graphics g, String text,int x, int y) {
        g.setColor(Color.BLACK);
        FontMetrics metrics = g.getFontMetrics();
        x = x - (metrics.stringWidth(text)/2);
        g.drawString(text, x, y);
    }
    
    public void drawCenteredVString(Graphics g, String text,int x, int y,int alpha) {
        g.setColor(new Color(0,0,0, alpha));
        FontMetrics metrics = g.getFontMetrics();
        y = y + (metrics.getMaxAscent()/2);
        g.drawString(text, x, y);
    }
    
    public void drawNode(Graphics g,int x, int y,Color nodeColor){
        int w=nodeDiam;
        int h=nodeDiam;
        g.setColor(edgeColor);
        int oversize = 6;
        g.fillOval(x-(w+oversize)/2, y-(h+oversize)/2, w+oversize, h+oversize);
        g.setColor(nodeColor);
        g.fillOval(x-w/2, y-h/2, w, h);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }
    
    public void setEdEdgAL(ActionListener al){
        this.ededgAL=al;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(grab){
            grabbingX=e.getX();
            grabbingY=e.getY();
            //repaint();
        }
    }

    public void setGrab(boolean grab) {
        this.grab = grab;
    }
    
    public void setDijkstraListener(ActionListener dijAL){
        this.dijAL=dijAL;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        clearPath();
        if(!edgeMode && e.getButton()==MouseEvent.BUTTON1){
            for (int i = 0; i < nodes.size(); i++) {
            if(draggedNode == -1 && checkInNode(i, e)){
                draggedNode=i;
                Point np = nodePoints.get(i);
                Point mouseP = e.getPoint();
                draggedDif=new Point(mouseP.x-np.x, mouseP.y-np.y);
                break;
            }
        }
        }
        if(grab){
            grabbingX=-10;
            grabbingY=-10;
            saveNode(e.getX(),e.getY());
            grab=false;
            confirmNodeAL.actionPerformed(null);
        }
        for (int i = 0; i < nodePoints.size(); i++){
            if(!edgeMode && !dijMode && checkInNode(i, e) && e.getClickCount()==2){
                edAL.actionPerformed(new ActionEvent(nodes.get(i), 0, "editNode"));
            }
            if((selected.get(i) || checkInNode(i, e)) && e.getButton()==MouseEvent.BUTTON3){
                System.out.println("");
                if(delAL!=null) delAL.actionPerformed(new ActionEvent(nodes.get(i), 0, "DEL_NODE"));
                delete(i);
            }
            if(edgeModeSelected && (selected.get(i) || checkInNode(i, e)) && e.getButton()==MouseEvent.BUTTON1){//segunda arista
                //agregar al nodo las aristas
                Node n = nodes.get(nodeSelected);
                Node b = nodes.get(i);
                Edge<String> eda = n.linkDouble(b,currentDist);
                edgesPoints.add(new Point[]{nodePoints.get(nodeSelected),nodePoints.get(i)});
                edges.add(eda);
                edgeModeSelected=false;
                edgeMode=false;
                dists.add(currentDist);
                alE.actionPerformed(null);
                repaint();  
            }
            if(edgeMode && (selected.get(i) || checkInNode(i, e)) && e.getButton()==MouseEvent.BUTTON1){//primera arista
                if(!edgeModeSelected){
                    paintMessage("Seleccione el vertice 2", false);
                    nodeSelected=i;
                    edgeModeSelected=true;
                }
            }
            if(dijModeSelected && (selected.get(i) || checkInNode(i, e)) && e.getButton()==MouseEvent.BUTTON1){//segunda arista
                //agregar al nodo las aristas
                dispelMess();
                Node n = nodes.get(dijSelected);
                Node b = nodes.get(i);
                dijAL.actionPerformed(new ActionEvent(new Node[]{n,b}, 0, "dij"));
                dijModeSelected=false;
                dijMode=false;
                repaint();  
            }
            if(dijMode && (selected.get(i) || checkInNode(i, e)) && e.getButton()==MouseEvent.BUTTON1){//primera arista
                if(!edgeModeSelected){
                    paintMessage("Seleccione el vertice 2", false);
                    dijSelected=i;
                    dijModeSelected=true;
                }
            }
        }
        for (int i = 0; i < edgesPoints.size(); i++) {
            if(!edgeMode && !dijMode && checkInEdge(i, e) && e.getClickCount()==2){
                ededgAL.actionPerformed(new ActionEvent(new double[]{dists.get(i),(double)i}, 0, "editEdge"));
            }
            if(checkInEdge(i, e) && e.getButton()==MouseEvent.BUTTON3){
                deleteEdge(i);
            }
        }
    }
    
    public void addDelListener(ActionListener al){
        this.delAL = al;
    }
    
    public void delete(int i){
        Point p = nodePoints.get(i);
        ArrayList<Point[]> pointsToDelete = new ArrayList<>();
        ArrayList<Double> distsToDelete = new ArrayList<>();
        ArrayList<Edge<String>> edgesToDelete = new ArrayList<>();
        for (int j = 0; j < edgesPoints.size(); j++) {
            Point[] edge=edgesPoints.get(j);
            if(edge[0]==p || edge[1]==p){
                pointsToDelete.add(edge);
                distsToDelete.add(dists.get(i));
                edgesToDelete.add(edges.get(i));
            }
        }
        edgesPoints.removeAll(pointsToDelete);
        edges.removeAll(edgesToDelete);
        dists.removeAll(distsToDelete);
        
        selected.remove(i);
        nodePoints.remove(i);
        nodes.remove(i);
        clearPath();
        repaint();
    }
     /**
      * 
      * @param i edge index for edgePoints and edges
      */
    public void deleteEdge(int i){
        overEdge=-1;
        Point[] edge = edgesPoints.get(i);
        for (int j = 0; j < nodePoints.size(); j++) {
            Node n = nodes.get(j);
            Point p = nodePoints.get(i);
            if(edge[0]==p){//si el punto del nodo origen coincide con el punto almacenado en la arista (edge)
                System.out.println("flag 1: "+n);
                for (int k = 0; k < nodePoints.size(); k++) {
                    Node n2 = nodes.get(k);
                    Point p2 = nodePoints.get(k);
                    System.out.println("flag 1: "+n);
                    System.out.println(edge[1]+" = "+p2);
                    if(edge[1]==p2){//si el punto destino coincide
                        System.out.println(n.unlinkDouble(edges.get(i), n2));
                        break;  
                    }
                }
            }
            break;
        }
        edgesPoints.remove(i);
        clearPath();
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        draggedNode=-1;
        draggedDif=null;
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    private void mouseOverEdge(){
        if(draggedNode==-1){
            Point mousePosition = this.getMousePosition();
            if(mousePosition!=null){
                int x = mousePosition.x;
                int y = mousePosition.y;
                for (int i = 0; i < edgesPoints.size(); i++) {
                    Point[] points = edgesPoints.get(i);
                    Point p = midPoint(points[0],points[1]);
                    double dist = distance(x, y, p.x, p.y);
                    int w=15;
                    if(dist<w/2){
                        overEdge=i;
                        break;
                    }else{
                        overEdge=-1;
                    }
                }
            }
        }
    }
    
    private void mouseOverNode() {
        Point mousePosition = this.getMousePosition();
        if(mousePosition != null){
            int x = mousePosition.x;
            int y = mousePosition.y;
            boolean in = false;
            for (int i = 0; i < nodePoints.size(); i++) {
                Point p = nodePoints.get(i);
                double dist = distance(x, y, p.x, p.y);
                if(dist<nodeDiam/2){
                    if(!selected.get(i))
                        in=true;
                    selected.set(i, true);
                }else{
                    if(selected.get(i))
                        in=true;
                    selected.set(i, false);
                }
            }
            if(in) repaint();
        }
    }
    
    private boolean checkInEdge(int i, MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        if(!edgesPoints.isEmpty()){
            Point p = midPoint(edgesPoints.get(i)[0],edgesPoints.get(i)[1]);
            double dist = distance(x, y, p.x, p.y);
            return (dist<15/2);
        }else{
            return false;
        }
    }
    
    public boolean checkInNode(int i, MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        Point p = nodePoints.get(i);
        double dist = distance(x, y, p.x, p.y);
        return (dist<nodeDiam/2);
    }
    
    public double distance(double x1,double y1,double x2,double y2) {       
          return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    public void confirmEvent(ActionListener a){
        this.confirmNodeAL=a;
    }
    
    void desacDrag() {
        grab=false;
        //if(selected.size()>0) names.remove(names.size()-1);
        repaint();
    }

    public double getCurrentDist() {
        return currentDist;
    }

    public void setCurrentDist(double currentDist) {
        this.currentDist = currentDist;
    }

    void editNode(Node n, String newInfo) {
        for (Node node : nodes) {
            if(node.equals(n)){
                node.setInfo(newInfo);
            }
        }
    }

    void editEdge(double n, double edgeSize) {
        edges.get((int)n).setDistance(edgeSize);
        Edge<String> eop = edges.get((int)n).getOpossite();
        if(eop!=null)
            eop.setDistance(edgeSize);
        dists.set((int)n, edgeSize);
    }

    void reset() {
        nodePoints.clear();
        edgesPoints.clear();
        edges.clear();
        dists.clear();
        nodes.clear();
        if(path!=null) path.clear();
        selected.clear();
        repaint();
    }

    void clearPath() {
        if(path != null) path.clear();
        pathMode=false;
        currentLabel=0;
        simMode=false;
    }

    void setPathListener(ActionListener actionListener) {
        this.pathAL=actionListener;
    }

    public ArrayList<Node<String>> getNodes() {
        return nodes;
    }   
}