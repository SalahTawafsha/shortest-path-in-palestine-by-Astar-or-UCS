// Author: Salah Tawafsha
// Uniform-cost search Algorithm implementation
package Algorithms;

import data_structures.Node;
import records.City;

import java.util.*;

public class UCS implements ShortestPath {

    @Override
    public Node shortestPath(String start, String target, Map<City, LinkedList<Node>> graph) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Set<Node> closedList = new HashSet<>();

        queue.add(new Node(get(start, graph), 0.0));

        while (!queue.isEmpty()) {          // O(V+E)
            Node curr = queue.remove();

            if (curr.getCity().name().equals(target))
                return curr;

            closedList.add(curr);

            LinkedList<Node> adj = graph.get(curr.getCity());
            for (Node node : adj) {

                if (!closedList.contains(node)) {
                    Node n = new Node(node.getCity(), node.getG() + curr.getG(), node.getG() + curr.getG(), curr);
                    queue.add(n);
                    closedList.add(node);
                }
            }

        }
        return null;
    }

    private City get(String cityName, Map<City, LinkedList<Node>> graph) {
        Set<City> cities = graph.keySet();

        for (City one : cities) {
            if (one.name().equals(cityName)) {
                return one;
            }
        }
        return new City(cityName, 0, 0);
    }

}
