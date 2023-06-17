// Author: Salah Tawafsha
// this class reads Heuristic and have method to get heuristic from city to destination
package Heuristic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Heuristic {
    private final Map<String, Double> airDistances = new HashMap<>();
    private static Heuristic heuristic;

    private Heuristic() {
        try {
            Scanner scan = new Scanner(new File("air distance.csv"));
            while (scan.hasNextLine()) {
                String[] tokens = scan.nextLine().split(",");
                airDistances.put(tokens[0].trim() + tokens[1].trim(), Double.parseDouble(tokens[2].trim()));
                airDistances.put(tokens[1].trim() + tokens[0].trim(), Double.parseDouble(tokens[2].trim()));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Can't find the file of Air Distance.");
        }
    }

    // get instance of heuristic
    public static Heuristic getInstance() {
        if (heuristic == null)
            heuristic = new Heuristic();
        return heuristic;
    }

    public double getHeuristic(String sourceName, String destinationName) {
        if (sourceName.equals(destinationName))
            return 0;

        return airDistances.get(sourceName + destinationName);
    }


}
