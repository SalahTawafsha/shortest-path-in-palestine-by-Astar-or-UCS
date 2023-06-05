// Author: Salah Tawafsha
// record to store info that will be displayed on table view of path
package records;

public record TableNode(String source, String destination, Double cost) {

    @Override
    public String toString() {
        return "TableNode{" +
                "source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", cost=" + cost +
                '}';
    }
}
