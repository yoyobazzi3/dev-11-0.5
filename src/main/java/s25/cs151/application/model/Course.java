package s25.cs151.application.model;

public record Course(String code, String name, String section) {
    @Override public String toString() { return code + "-" + section; }
}
