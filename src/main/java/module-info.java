module com.example.astar {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.astar to javafx.fxml;
    exports com.example.astar;
    exports Heuristic;
    opens Heuristic to javafx.fxml;
    exports data_structures;
    opens data_structures to javafx.fxml;
    exports records;
    opens records to javafx.fxml;
    exports Algorithms;
    opens Algorithms to javafx.fxml;
}