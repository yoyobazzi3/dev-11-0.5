package s25.cs151.application.model;

public record Course(String code, String name, String section) implements BaseEntry {
    @Override
    public String toString() {
        return code + "-" + section;
    }

    @Override
    public String display() {
        return code + " (" + name + ") — sec " + section;
    }
}
