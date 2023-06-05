// Author: Salah Tawafsha
// Controller class for the main window
package com.example.astar;

import Algorithms.AStar;
import Algorithms.BFS;
import Algorithms.ShortestPath;
import Heuristic.Heuristic;
import data_structures.Node;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import records.City;
import records.TableNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    private RadioButton bfs;

    @FXML
    private RadioButton aStar;

    @FXML
    private ComboBox<String> destinationBox;

    @FXML
    private ComboBox<String> sourceBox;
    @FXML
    private TableView<TableNode> tableView;
    @FXML
    private TableColumn<TableNode, Double> costCol;

    @FXML
    private TableColumn<TableNode, String> destinationCol;

    @FXML
    private TableColumn<TableNode, String> sourceCol;

    @FXML
    private Label time;

    private final static Heuristic heuristic = new Heuristic();
    private final static HashMap<City, LinkedList<Node>> graph = new HashMap<>();


    @FXML
    void find() {
        if (sourceBox.getValue() != null && destinationBox.getValue() != null)
            findShortestPath();
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please fill all inputs!");
            alert.setContentText("You must select Source and Destination!");
            alert.show();
        }
    }

    private void findShortestPath() {
        ShortestPath shortestPath;      // interface to just define the method shortestPath and not the implementation
        if (aStar.isSelected())         // select implementation Class
            shortestPath = new AStar();
        else
            shortestPath = new BFS();

        long currTime = System.currentTimeMillis();     // to calculate the time taken to find the shortest path
        Node n = shortestPath.shortestPath(sourceBox.getValue(), destinationBox.getValue());
        time.setText("Time taken: " + (System.currentTimeMillis() - currTime) + "ms");   // set the time taken to the label

        ArrayList<TableNode> tableNodes = new ArrayList<>();    // to display the path in the table
        if (n != null) {
            tableNodes.add(new TableNode("Total", "", n.getG())); // store total distance
            Node curr = n;
            for (; curr.getParent() != null; curr = curr.getParent())   // add each node with its parent and cost
                tableNodes.add(new TableNode(curr.getParent().getCity().name(), curr.getCity().name(), curr.getG() - curr.getParent().getG()));
        } else
            tableNodes.add(new TableNode("No path found", "", Double.MAX_VALUE));

        Collections.reverse(tableNodes);
        tableView.setItems(FXCollections.observableArrayList(tableNodes));
    }


    public static Heuristic getHeuristic() {
        return heuristic;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup toggleGroup = new ToggleGroup();
        bfs.setToggleGroup(toggleGroup);
        aStar.setToggleGroup(toggleGroup);

        ObservableList<String> cities = heuristic.getCities();
        sourceBox.setItems(cities);
        destinationBox.setItems(cities);

        sourceCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().source()));
        destinationCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().destination()));
        costCol.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().cost()).asObject());
        tableView.setSelectionModel(null);

        readCities();

        readRoads();
    }

    private void readRoads() {
        try {
            Scanner scan = new Scanner(new File("roads.csv"));
            while (scan.hasNextLine()) {
                String[] tokens = scan.nextLine().split(",");
                graph.get(new City(tokens[0].trim())).add(new Node(new City(tokens[1].trim()), Double.parseDouble(tokens[2].trim())));
                graph.get(new City(tokens[1].trim())).add(new Node(new City(tokens[0].trim()), Double.parseDouble(tokens[2].trim())));
            }
//            System.out.println(graph);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void readCities() {
        try {
            Scanner scan = new Scanner(new File("cities.csv"));
            while (scan.hasNextLine()) graph.put(new City(scan.nextLine().trim()), new LinkedList<>());


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    public static HashMap<City, LinkedList<Node>> getGraph() {
        return graph;
    }
}
