package s25.cs151.application.model;

public record TimeSlot(String from, String to) {
    @Override public String toString() { return from + " – " + to; }
}
