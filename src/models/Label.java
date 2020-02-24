/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author León
 */
public class Label<T> extends Edge<T>{

    private boolean visited;
    private int iteration;

    public Label(Node<T> a, Node<T> b, double distance,int iteration) {
        super(a, b, distance);
        visited=false;
        this.iteration=iteration;
    }
    
    public Label(Node<T> a, Node<T> b, double distance,int iteration,boolean visited) {
        super(a, b, distance);
        this.iteration=iteration;
        this.visited=visited;
    }
    
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    @Override
    public String toString() {
        return "Label{"+super.toString() + "visited=" + visited + '}';
    }
    
    public Label clone(){
        return new Label(new Node<T>(a==null?null:a.info), new Node<T>(b==null?null:b.info), distance, iteration,visited);
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public int getIteration() {
        return iteration;
    }
}
