package data_structures;

import records.City;

import java.util.Objects;

public final class Node implements Comparable<Node> {
    private final City city;
    private Double f;
    private Double g;
    private Double h;
    private Node parent;

    public Node(City city, Double cost) {
        this.city = city;
        this.f = cost;
        this.g = cost;
    }

    public Node(City city, Double f, Double g, Double h, Node parent) {
        this.city = city;
        this.f = f;
        this.g = g;
        this.h = h;
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

    public void setG(Double g) {
        this.g = g;
    }

    public Double getH() {
        return h;
    }

    public void setH(Double h) {
        this.h = h;
    }

    public void setF(Double f) {
        this.f = f;
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
        return Objects.equals(this.city, that.city) &&
                Objects.equals(this.f, that.f);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, f);
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
