package Algorithms;

import com.example.astar.MainController;
import data_structures.Node;
import records.City;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class AStar implements ShortestPath {
    private static final HashMap<City, LinkedList<Node>> graph = MainController.getGraph();

    public Node shortestPath(String start, String target) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        LinkedList<Node> closedList = new LinkedList<>();

        queue.add(new Node(new City(start), 0.0));
        while (!queue.isEmpty()) {
            Node curr = queue.remove();
            closedList.add(curr);
            if (curr.getCity().name().equals(target))
                return curr;

            LinkedList<Node> adj = graph.get(curr.getCity());
            for (Node node : adj) {

                if (closedList.contains(node)) {
                    continue;
                }

                double gScore = node.getG() + curr.getG();
                double heuristicScore = MainController.getHeuristic().getHeuristic(node.getCity().name(), target);

                if (!queue.contains(node) || gScore + heuristicScore < node.getF()) {
                    Node n = new Node(node.getCity(), gScore + heuristicScore, gScore, heuristicScore, curr);
                    queue.add(n);
                }
            }

        }
        return null;
    }
}
