package records;

public class TableNode {
    private String source;
    private String destination;
    private Double cost;

    public TableNode(String source, String destination, Double cost) {
        this.source = source;
        this.destination = destination;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "TableNode{" +
                "source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", cost=" + cost +
                '}';
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public Double getCost() {
        return cost;
    }
}
