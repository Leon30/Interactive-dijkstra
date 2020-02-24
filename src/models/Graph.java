/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import util.ArraysUtilities;

/**
 *
 * @author ACWIN7C140
 */
public class Graph<T> {
    
    private List<Node<T>> n;
    private double shortestPastDist=-1;
    private ArrayList<ArrayList<Label<T>>> simLabels;

    public Graph(List<Node<T>> n) {
        this.n = n;
    }
    
    public Graph() {
        this.n = new ArrayList<>();
    }
    
    public void linkDouble(T t1,T t2,int dist){
        if(t1==t2) return;
        if(t1==null || t2==null) return;
        Node a = searchNode(t1);
        Node b = searchNode(t2);
        if(a!=null & b != null){
            a.link(b, dist);
            b.link(a, dist);
        }
    }
    
    public void link(T t1,T t2,int dist){
        if(t1==t2) return;
        if(t1==null || t2==null) return;
        Node a = null;
        Node b = null;
        for (Node<T> n1 : n) {
            if(n1.info.equals(t1)){
               a=n1; 
            }
            if(n1.info.equals(t2)){
               b=n1;
            }
        }
        if(a!=null & b != null) a.link(b, dist);
    }
    
    private Node<T> searchNode(T info){
        for (Node<T> n1 : n) {
            if(n1.info.equals(info)){
                return n1;
            }
        }
        return null;
    }
    
    public ArrayList<T> dijkstra(T t1,T t2){
        System.out.println("de "+t1+" a "+t2);
        return dijkstra(searchNode(t1), searchNode(t2));
    }
    
    private ArrayList<T> dijkstra(Node<T> a,Node<T> b){
        simLabels=new ArrayList<>();
        ArrayList<Label<T>> labels = new ArrayList<>();
        for (int i = 0; i < n.size(); i++) {
            if(n.get(i).info.equals(a.info)){
                labels.add(new Label(a, null, 0,0));
            }else
                labels.add(new Label<T>(n.get(i), null, Double.MAX_VALUE,0));
        }
        int visited = 0;
        int i =0;
        simLabels.add(new ArraysUtilities<T>().cloneLabels(labels));
        while(visited < n.size()){
            System.out.println("Labels: "+Arrays.toString(labels.toArray()));
            System.out.println("Iteracion: "+(i));
            Label<T> min = searchMinLabel(labels);
            System.out.println("Menor: "+min);
            //Etiquetar labels adjacentes
            ArrayList<Edge<T>> realadjs = (ArrayList<Edge<T>>) min.a.edges;//buscar aristas del vertice
            System.out.println("Adyasencia real: "+Arrays.toString(realadjs.toArray()));
            ArrayList<Label<T>> labelsAdjs = searchAdjs(realadjs,labels);//correspondela con labels
            System.out.println("Adyacencia en labels: "+Arrays.toString(labelsAdjs.toArray()));
            for (int j = 0; j < labelsAdjs.size(); j++){
                Label<T> labelsAdjs1 = labelsAdjs.get(j);
                Edge<T> adjsReal = searchRealEdge(labelsAdjs1, realadjs);//arista real entre min y labelsAdjs1
                System.out.println("Curr lab: "+labelsAdjs1);
                System.out.println("Curr  real lab: "+adjsReal);
                System.out.println(min.distance+"+"+adjsReal.distance+" Menor que "+labelsAdjs1.distance);
                if(!labelsAdjs1.isVisited() && (min.distance+adjsReal.distance) < labelsAdjs1.distance){
                    System.out.println("\tSi");
                    labelsAdjs1.distance=min.distance+adjsReal.distance;
                    labelsAdjs1.b=min.a;
                    System.out.println("\t"+labelsAdjs1.toString());
                }
            }
            min.setIteration(i);
            i++;
            //visitado
            min.setVisited(true);
            visited++;
            simLabels.add(new ArraysUtilities<T>().cloneLabels(labels));
        }
        ArrayList<T> path=new ArrayList<>();
        Label<T> blabel = null;
        for (Label<T> label : labels) {
            if(label.a == b){
                System.out.println("label "+label+" encontrada " +"("+b+")");
                blabel=label;
                break;
            }
        }
        shortestPastDist=blabel.distance;
        
        System.out.println("Final Labels: "+Arrays.toString(labels.toArray()));
        Label<T> auxLabel = blabel;
        while (auxLabel.b!=null) {
            path.add(auxLabel.a.info);
            System.out.println("Recorriendo: "+auxLabel);
            //buscar label con a en b
            for (Label<T> label : labels) {
                System.out.println("lb:"+label);
                if(auxLabel.b==null || auxLabel.b.equals(label.a)){
                    auxLabel=label;
                    break;
                }
            }
        }
        path.add(a.info);
        return path;
    }
    
    public double getShortestPathDist(){
        if(shortestPastDist!=-1){
            double aux = shortestPastDist;
            shortestPastDist=-1;
            return aux;
        }
        return -1;
    }
    
    public Edge<T> searchRealEdge(Label<T> label1,ArrayList<Edge<T>> list){
        for (Edge<T> list1 : list) {
            if(list1.b.equals(label1.a)){
                return list1;
            }
        }
        return null;
    }
    
    /**
     * Busca etiquetas correspondientes de una lista de adyasencia de un vertice
     * @param adjsToSearch
     * @param adjs 
     */
    public ArrayList<Label<T>> searchAdjs(ArrayList<Edge<T>> adjsToSearch, ArrayList<Label<T>> labels){
        ArrayList<Label<T>> re = new ArrayList<>();
        for (Edge<T> adj : adjsToSearch) {
            for (Edge<T> label : labels) {
                if(adj.b.equals(label.a)){
                    re.add((Label<T>) label);
                }
            }
        }
        return re;
    }
    
    private Label<T> searchMinLabel(ArrayList<Label<T>> labels){
        Label<T> min = null;
        for (Label<T> label : labels) {
            if((min==null || label.distance<min.distance) && !label.isVisited()){
                min = label;
            }
        }
        System.out.println(Arrays.toString(labels.toArray())+" Minimo: "+min.toString());
        return min;
    }

    public void setN(List<Node<T>> n) {
        this.n = n;
    }

    public List<Node<T>> getN() {
        return n;
    }
    
    public static void main(String[] args) {
        Graph<String> g = new Graph<>();
        g.n.add(new Node<>("0"));
        g.n.add(new Node<>("1"));
        g.n.add(new Node<>("2"));
        g.n.add(new Node<>("3"));
        g.n.add(new Node<>("4"));
        g.n.add(new Node<>("5"));
        g.n.add(new Node<>("6"));
        g.n.add(new Node<>("7"));
        g.n.add(new Node<>("8"));
        
        g.link("0", "1", 4);
        g.link("0", "7", 8);
        g.link("1", "2", 8);
        g.link("1", "0", 4);
        g.link("1", "7", 11);
        g.link("2", "1", 8);
        g.link("2", "3", 7);
        g.link("2", "5", 4);
        g.link("2", "8", 2);
        g.link("3", "2", 7);
        g.link("3", "4", 9);
        g.link("3", "5", 14);
        g.link("4", "3", 9);
        g.link("4", "5", 10);
        g.link("5", "4", 10);
        g.link("5", "3", 14);
        g.link("5", "2", 4);
        g.link("5", "6", 4);
        g.link("6", "5", 2);
        g.link("6", "8", 6);
        g.link("6", "7", 1);
        g.link("8", "2", 2);
        g.link("8", "7", 7);
        g.link("8", "6", 6);
        g.link("7", "0", 8);
        g.link("7", "1", 11);
        g.link("7", "8", 7);
        g.link("7", "6", 1);
        
        g.printGraph();
        
        System.out.println("Dijkstra: " + g.dijkstra("0", "4"));
    }

    public void printGraph() {
        System.out.println("grafo: ");
        for (Node<T> n : n) {
            System.out.print(n.info+": ");
            System.out.println(Arrays.toString(n.edges.toArray()));
        }
    }

    public ArrayList<ArrayList<Label<T>>> getSimLabels() {
        return simLabels;
    }
}
