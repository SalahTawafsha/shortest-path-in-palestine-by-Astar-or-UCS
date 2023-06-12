// Author: Salah Tawafsha
// record to store name of city as object of city
package records;

import java.util.Objects;

public record City(String name, double latitude , double longitude) {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof City)
            return name.equals(((City) (obj)).name);
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

}
