// Author: Salah Tawafsha
// A* Algorithm implementation
package Algorithms;

import com.example.astar.MainController;
import data_structures.Node;
import records.City;

import java.util.*;

public class AStar implements ShortestPath {
    private static final HashMap<City, LinkedList<Node>> graph = MainController.getGraph();

    public Node shortestPath(String start, String target) {
        PriorityQueue<Node> queue = new PriorityQueue<>();      // priority queue to delete min with O(log n)
        Set<Node> closedList = new HashSet<>();                 // to store what we visit

        queue.add(new Node(get(start), 0.0));
        while (!queue.isEmpty()) {          // O(b^d)
            Node curr = queue.remove();

            if (closedList.contains(curr))
                continue;

            closedList.add(curr);
            if (curr.getCity().name().equals(target))
                return curr;

            LinkedList<Node> adj = graph.get(curr.getCity());
            for (Node node : adj) {

                if (closedList.contains(node))
                    continue;


                double gScore = node.getG() + curr.getG();
                double heuristicScore = MainController.getHeuristic().getHeuristic(node.getCity().name(), target);

                if (!queue.contains(node) || gScore + heuristicScore < node.getF()) {
                    Node n = new Node(node.getCity(), gScore + heuristicScore, gScore, curr);
                    queue.add(n);
                }
            }

        }
        return null;
    }

    private City get(String cityName) {
        Set<City> cities = graph.keySet();

        for (City one : cities) {
            if (one.name().equals(cityName)) {
                return one;
            }
        }
        return new City(cityName, 0, 0);
    }
}
