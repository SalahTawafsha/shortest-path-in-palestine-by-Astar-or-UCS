// Author: Salah Tawafsha
// Controller class for the main window
package com.example.astar;

import Algorithms.AStar;
import Algorithms.UCS;
import Algorithms.ShortestPath;
import data_structures.Node;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import records.City;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    private TextArea path;
    @FXML
    private Pane lines;
    @FXML
    private RadioButton ucs;

    @FXML
    private RadioButton aStar;

    @FXML
    private ComboBox<String> destinationBox;

    @FXML
    private ComboBox<String> sourceBox;

    @FXML
    private Label time;
    @FXML
    private TextField distance;
    @FXML
    private Pane pane;

    private final static HashMap<City, LinkedList<Node>> graph = new HashMap<>();
    private static final double PIC_WIDTH = 372;
    private static final double PIC_HEIGHT = 639;


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
            shortestPath = new UCS();

        long currTime = System.currentTimeMillis();     // to calculate the time taken to find the shortest path

        Node n = shortestPath.shortestPath(sourceBox.getValue(), destinationBox.getValue(), graph);

        time.setText("Time taken: " + (System.currentTimeMillis() - currTime) + "ms");   // set the time taken to the label

        drawLines(n);

    }

    private void drawLines(Node n) {
        lines.getChildren().clear();

        if (n != null) {
            distance.setText(String.valueOf(n.getG()));

            StringBuilder path = new StringBuilder(n.getCity().name());

            for (Node curr = n; curr.getParent() != null; curr = curr.getParent()) {  // add each node with its parent and cost
                Line l = new Line(getX(curr.getCity().longitude()), getY(curr.getCity().latitude()),
                        getX(curr.getParent().getCity().longitude()), getY(curr.getParent().getCity().latitude()));
                l.setStrokeWidth(2);

                lines.getChildren().add(l);
                path.insert(0, curr.getParent().getCity().name() + " -> " + (curr.getG() - curr.getParent().getG()) + "\n⬇\n");

            }
            this.path.setText(path.toString());
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup toggleGroup = new ToggleGroup();
        ucs.setToggleGroup(toggleGroup);
        aStar.setToggleGroup(toggleGroup);

        readCities();

        readRoads();

        ObservableList<String> cities = FXCollections.observableArrayList(new ArrayList<>(graph.keySet()).stream().map(City::name).toList());
        sourceBox.setItems(cities);
        destinationBox.setItems(cities);

        drawCities();

    }

    private void drawCities() {
        for (City one : graph.keySet()) {
            Button b = new Button(one.name());
            b.setStyle("-fx-font-size: 5; -fx-max-height: 10;-fx-max-width: 10");
            b.setLayoutY(getY(one.latitude()));
            b.setLayoutX(getX(one.longitude()));
            b.hoverProperty().addListener(showLabel(b));

            b.setOnMouseClicked(select(b));

            pane.getChildren().add(b);
        }
    }

    private ChangeListener<? super Boolean> showLabel(Button b) {
        return (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (b.isHover()) {
                Label l = new Label(b.getText());
                l.setLayoutY(b.getLayoutY());
                l.setLayoutX(b.getLayoutX());
                l.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #d8d9e0, lightgray); " +
                                "-fx-border-radius: 50;fx-border-color: rgb(128,128,128);-fx-background-radius: 20;-fx-padding: 2.5");


                l.hoverProperty().addListener(e1 -> {
                    if (!l.isHover())
                        pane.getChildren().remove(l);
                });

                l.setOnMouseClicked(select(l));

                pane.getChildren().add(l);

            }
        };
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

    public void clear() {
        lines.getChildren().clear();
        sourceBox.setValue(null);
        destinationBox.setValue(null);
        path.setText("");
        distance.setText("");
        time.setText("");
    }
}
