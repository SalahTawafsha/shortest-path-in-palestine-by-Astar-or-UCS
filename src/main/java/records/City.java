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
