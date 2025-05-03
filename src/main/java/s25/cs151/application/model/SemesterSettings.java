package s25.cs151.application.model;

import java.util.Set;

public record SemesterSettings(String semester, String year, Set<String> days) { }
