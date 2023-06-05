// Author: Salah Tawafsha
// record to store name of city as object of city
package records;

public record City(String name) {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof City)
            return name.equals(((City) (obj)).name);
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

}
