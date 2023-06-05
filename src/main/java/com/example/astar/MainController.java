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
            if (heuristic.getHeuristic(sourceBox.getValue(), destinationBox.getValue()) != 0) {
                findShortestPath();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Distance is Zero!");
                alert.setContentText("Nice try, you can't go to the same city!");
                alert.show();
            }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fill all inputs!");
            alert.setContentText("You must select Source and Destination!");
            alert.show();
        }
    }

    private void findShortestPath() {
        ShortestPath shortestPath;
        if (aStar.isSelected())
            shortestPath = new AStar();
        else
            shortestPath = new BFS();
        long currTime = System.currentTimeMillis();
        Node n = shortestPath.shortestPath(sourceBox.getValue(), destinationBox.getValue());
        time.setText("Time taken: " + (System.currentTimeMillis() - currTime) + "ms");
        ArrayList<TableNode> tableNodes = new ArrayList<>();
        if (n != null) {
            tableNodes.add(new TableNode("Total", "", n.getG()));
            Node curr = n;
            for (; curr.getParent() != null; curr = curr.getParent())
                tableNodes.add(0, new TableNode(curr.getParent().getCity().name(), curr.getCity().name(), curr.getG() - curr.getParent().getG()));

        } else
            tableNodes.add(new TableNode("No path found", "", Double.MAX_VALUE));

//        System.out.println(tableNodes);
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

        sourceCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSource()));
        destinationCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDestination()));
        costCol.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().getCost()).asObject());

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
