// Author: Salah Tawafsha
// store City that edge go to it, f, g and parent that for find path
package data_structures;

import records.City;

import java.util.Objects;

public final class Node implements Comparable<Node> {
    private final City city;
    private final Double f;
    private final Double g;
    private final Node parent;

    public Node(City city, Double cost) {
        this.city = city;
        this.f = cost;
        this.g = cost;
        this.parent = null;
    }

    public Node(City city, Double f, Double g, Node parent) {
        this.city = city;
        this.f = f;
        this.g = g;
        this.parent = parent;
    }

    @Override
    public String toString() {
        return city +
                "\t" + f;
    }

    @Override
    public int compareTo(Node o) {
        return f.compareTo(o.f);
    }

    public Node getParent() {
        return parent;
    }

    public Double getG() {
        return g;
    }

    public City getCity() {
        return city;
    }

    public Double getF() {
        return f;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Node) obj;
        return Objects.equals(this.city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, f);
    }

}
