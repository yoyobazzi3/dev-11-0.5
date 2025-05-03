package s25.cs151.application.model;

public record ScheduledOfficeHour(
        String student, String date, String time, String course,
        String reason,  String comment) { }
