package s25.cs151.application.model;

public record TimeSlot(String from, String to) implements BaseEntry {
    @Override
    public String toString() {
        return from + " – " + to;
    }

    @Override
    public String display() {
        return "From " + from + " to " + to;
    }
}
