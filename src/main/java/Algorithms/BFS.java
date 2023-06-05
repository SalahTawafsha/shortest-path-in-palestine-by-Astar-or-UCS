// Author: Salah Tawafsha
// BFS Algorithm implementation
package Algorithms;

import com.example.astar.MainController;
import data_structures.Node;
import records.City;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class BFS implements ShortestPath {
    private final HashMap<City, LinkedList<Node>> graph = MainController.getGraph();

    @Override
    public Node shortestPath(String start, String target) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        LinkedList<Node> closedList = new LinkedList<>();

        queue.add(new Node(new City(start), 0.0));

        while (!queue.isEmpty()) {          // O(V+E)
            Node curr = queue.remove();

            closedList.add(curr);
            if (curr.getCity().name().equals(target))
                return curr;

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

}
