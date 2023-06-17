// Author: Salah Tawafsha
// interface to make polymorphism possible for the different shortest path algorithms
package Algorithms;

import data_structures.Node;
import records.City;

import java.util.LinkedList;
import java.util.Map;

public interface ShortestPath {
    Node shortestPath(String start, String target, Map<City, LinkedList<Node>> graph);
}
