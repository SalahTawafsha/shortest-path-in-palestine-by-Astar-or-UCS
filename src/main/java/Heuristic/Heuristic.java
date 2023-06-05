// Author: Salah Tawafsha
// this class reads Heuristic and have method to get heuristic from city to destination
package Heuristic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Heuristic {
    private final ArrayList<AirDistance> airDistances = new ArrayList<>();

    public Heuristic() {
        try {
            Scanner scan = new Scanner(new File("air distance.csv"));
            while (scan.hasNextLine()) {
                String[] tokens = scan.nextLine().split(",");
                airDistances.add(new AirDistance(tokens[0].trim(), tokens[1].trim(), Double.parseDouble(tokens[2].trim())));
                airDistances.add(new AirDistance(tokens[1].trim(), tokens[0].trim(), Double.parseDouble(tokens[2].trim())));
            }
            Collections.sort(airDistances);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Can't find the file of Air Distance.");
        }
    }

    public double getHeuristic(String sourceName, String destinationName) {
        if (sourceName.equals(destinationName))
            return 0;
        int i = Collections.binarySearch(airDistances, new AirDistance(sourceName, destinationName, 0));
        if (i >= 0)
            return airDistances.get(i).distance();
        else
            return -1;
    }

    public ObservableList<String> getCities() {
        ArrayList<String> cities = new ArrayList<>();

        for (AirDistance airDistance : airDistances) {
            if (!cities.contains(airDistance.sourceName()))
                cities.add(airDistance.sourceName());
        }
        return FXCollections.observableArrayList(cities);
    }

    private record AirDistance(String sourceName, String destinationName,
                               double distance) implements Comparable<AirDistance> {

        @Override
        public int compareTo(AirDistance o) {
            return (sourceName + destinationName).compareTo(o.sourceName() + o.destinationName());
        }
    }
}
