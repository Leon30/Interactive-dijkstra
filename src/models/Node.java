/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author ACWIN7C140
 */
public class Node<T> {

    T info;
    List<Edge<T>> edges;

    public Node(T info, List<Edge<T>> edges) {
        this.info = info;
        this.edges = edges;
    }

    public Node(T info) {
        this.info = info;
        edges=new ArrayList<>();
    }

    public T getInfo() {
        return info;
    }

    public List<Edge<T>> getEdges() {
        return edges;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public void setEdges(List<Edge<T>> edges) {
        this.edges = edges;
    }
    
    public Edge<T> link(Node b,double dist){
        Edge<T> e = new Edge(this, b, dist);
        edges.add(e);
        return e;
    }

    @Override
    public String toString() {
        return info.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node<?> other = (Node<?>) obj;
        if (!Objects.equals(this.info, other.info)) {
            return false;
        }
        return true;
    }

    public Edge<T> linkDouble(Node b, double currentDist) {
        Edge<T> e1 = new Edge(this, b, currentDist);
        Edge<T> e2 = new Edge(b, this, currentDist);
        e1.setOpossite(e2);
        e2.setOpossite(e1);
        edges.add(e1);
        b.edges.add(e2);
        return e1;
    }
    
    public boolean unlinkDouble(Edge<T> e, Node<T> n) {
        return edges.remove(e) && n.edges.remove(e.getOpossite());
    }
}
