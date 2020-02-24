/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Objects;

/**
 *
 * @author ACWIN7C140
 */
public class Edge<T> {

    Node<T> a;
    Node<T> b;
    Edge<T> opossite;
    double distance;

    public Edge(Node<T> a, Node<T> b, double distance) {
        this.a = a;
        this.b = b;
        this.distance = distance;
    }

    public Node<T> getA() {
        return a;
    }

    public Node<T> getB() {
        return b;
    }

    public double getDistance() {
        return distance;
    }

    public void setA(Node<T> a) {
        this.a = a;
    }

    public void setB(Node<T> b) {
        this.b = b;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Edge{" + "a=" + a + ", b=" + b + ", distance=" + distance + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Edge<?> other = (Edge<?>) obj;
        if (!Objects.equals(this.a, other.a)) {
            return false;
        }
        if (!Objects.equals(this.b, other.b)) {
            return false;
        }
        return true;
    }

    public void setOpossite(Edge<T> opossite) {
        this.opossite = opossite;
    }

    public Edge<T> getOpossite() {
        return opossite;
    }
    
}
