// Author: Salah Tawafsha
// interface to make polymorphism possible for the different shortest path algorithms
package Algorithms;

import data_structures.Node;

public interface ShortestPath {
    Node shortestPath(String start, String target);
}
