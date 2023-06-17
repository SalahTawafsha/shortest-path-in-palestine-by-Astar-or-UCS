// Author: Salah Tawafsha
// A* Algorithm implementation
package Algorithms;

import Heuristic.Heuristic;
import data_structures.Node;
import records.City;

import java.util.*;

public class AStar implements ShortestPath {

    public Node shortestPath(String start, String target, Map<City, LinkedList<Node>> graph) {
        PriorityQueue<Node> queue = new PriorityQueue<>();      // priority queue to delete min with O(log n)
        Set<Node> closedList = new HashSet<>();                 // to store what we visit

        queue.add(new Node(get(start, graph), 0.0));
        while (!queue.isEmpty()) {          // O(b^d), b is branching factor, d is depth
            Node curr = queue.remove();

            if (curr.getCity().name().equals(target))
                return curr;

            if (closedList.contains(curr))
                continue;

            closedList.add(curr);

            LinkedList<Node> adj = graph.get(curr.getCity());
            for (Node node : adj) { // O(b)

                if (closedList.contains(node))  // O(1)
                    continue;


                double gScore = node.getG() + curr.getG();
                double heuristicScore = Heuristic.getInstance().getHeuristic(node.getCity().name(), target);

                // if statment is 2 log n so O(log n)
                if (!queue.contains(node) || gScore + heuristicScore < node.getF()) {   // O(log n)
                    Node n = new Node(node.getCity(), gScore + heuristicScore, gScore, curr);
                    queue.add(n);   // O(log n)
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
