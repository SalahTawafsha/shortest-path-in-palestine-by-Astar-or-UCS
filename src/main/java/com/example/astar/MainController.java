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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import records.City;
import records.TableNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.security.cert.PolicyNode;
import java.util.*;

public class MainController implements Initializable {

    private static final double PIC_WIDTH = 372;
    private static final double PIC_HEIGHT = 639;
    @FXML
    private Pane lines;
    @FXML
    private RadioButton bfs;

    @FXML
    private RadioButton aStar;

    @FXML
    private ComboBox<String> destinationBox;

    @FXML
    private ComboBox<String> sourceBox;

    @FXML
    private Label time;

    private final static Heuristic heuristic = new Heuristic();
    private final static HashMap<City, LinkedList<Node>> graph = new HashMap<>();
    @FXML
    private Pane pane;


    @FXML
    void find() {
        if (sourceBox.getValue() != null && destinationBox.getValue() != null)
            findShortestPath();
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

        drawLines(n);

    }

    private void drawLines(Node n) {
        lines.getChildren().clear();

        if (n != null) {
//            tableNodes.add(new TableNode("Total", "", n.getG())); // store total distance

            Node curr = n;
            for (; curr.getParent() != null; curr = curr.getParent()) {  // add each node with its parent and cost
                Line l = new Line(getX(curr.getCity().longitude()), getY(curr.getCity().latitude()),
                        getX(curr.getParent().getCity().longitude()), getY(curr.getParent().getCity().latitude()));
                l.setStrokeWidth(2);

                lines.getChildren().add(l);
//                tableNodes.add(new TableNode(curr.getParent().getCity().name(), curr.getCity().name(), curr.getG() - curr.getParent().getG()));
            }
        } else
            time.setText("No path found");


    }

    private double getX(double longitude) {
        return (longitude - 34.5497326) / (35.814909 - 33.5) * PIC_WIDTH + 80;
    }

    private double getY(double latitude) {
        return PIC_HEIGHT - (latitude - 29.6331102) / (33.5267279 - 29.6331102) * PIC_HEIGHT - 20;
    }

    private EventHandler<? super MouseEvent> select(Labeled l) {
        return (EventHandler<MouseEvent>) event -> {
            if (l.isHover())
                if (sourceBox.getValue() == null) {
                    sourceBox.setValue(l.getText());
                } else if (sourceBox.getValue() != null && destinationBox.getValue() != null) {
                    sourceBox.setValue(l.getText());
                    destinationBox.setValue(null);

                } else {
                    destinationBox.setValue(l.getText());
                }
        };
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

        readCities();

        readRoads();

        for (City one : graph.keySet()) {
            Button b = new Button(one.name());
            b.setStyle("-fx-font-size: 5; -fx-max-height: 10;-fx-max-width: 10");
            double y = getY(one.latitude());
            double x = getX(one.longitude());
            b.setLayoutY(y);
            b.setLayoutX(x);
            b.hoverProperty().addListener(e -> {
                if (b.isHover()) {
                    Label l = new Label(b.getText());
                    l.setLayoutY(b.getLayoutY() - 10);
                    l.setLayoutX(b.getLayoutX());
                    l.setPadding(new Insets(2.5));
                    l.setStyle(
                            "-fx-background-color: pink;-fx-background-radius: 40;-fx-border-color: black;\n" +
                                    "-fx-border-radius: 40; -fx-alignment: center;-fx-font-size: 12;");


                    l.hoverProperty().addListener(e1 -> {
                        if (!l.isHover())
                            pane.getChildren().remove(l);
                    });

                    l.setOnMouseClicked(select(l));

                    pane.getChildren().add(l);

                }
            });

            b.setOnMouseClicked(select(b));

            pane.getChildren().add(b);
//            items.add(one.getName());
        }
    }

    private void readRoads() {
        try {
            Scanner scan = new Scanner(new File("roads.csv"));
            while (scan.hasNextLine()) {
                String[] tokens = scan.nextLine().split(",");
                graph.get(new City(tokens[0].trim(), 0, 0)).add(new Node(get(tokens[1].trim()), Double.parseDouble(tokens[2].trim())));
                graph.get(new City(tokens[1].trim(), 0, 0)).add(new Node(get(tokens[0].trim()), Double.parseDouble(tokens[2].trim())));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

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

    private void readCities() {
        try {
            Scanner scan = new Scanner(new File("cities.csv"));
            while (scan.hasNextLine()) {
                String[] tokens = scan.nextLine().split(",");

                graph.put(new City(tokens[0].trim(), Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])), new LinkedList<>());
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    public static HashMap<City, LinkedList<Node>> getGraph() {
        return graph;
    }
}
