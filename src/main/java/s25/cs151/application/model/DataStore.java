package s25.cs151.application.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public final class DataStore {

    private static final String COURSES_CSV   = "courses.csv";
    private static final String TIMES_CSV     = "time_slots.csv";
    private static final String SCHEDULE_CSV  = "scheduled_office_hours.csv";
    private static final String SEMESTER_CSV  = "office_hours_data.csv";

    // ---------- COURSES ----------
    public static ObservableList<Course> loadCourses() {
        ObservableList<Course> list = FXCollections.observableArrayList();
        Path p = Path.of(COURSES_CSV);
        if (Files.exists(p)) {
            try (BufferedReader br = Files.newBufferedReader(p)) {
                br.lines().map(l -> l.split(",", -1))
                        .filter(a -> a.length == 3)
                        .map(a -> new Course(a[0], a[1], a[2]))
                        .forEach(list::add);
            } catch (IOException ignored) {}
        }
        return list;
    }

    public static void saveCourses(List<Course> courses) {
        try (PrintWriter pw = new PrintWriter(COURSES_CSV)) {
            courses.forEach(c -> pw.println(
                    String.join(",", c.code(), c.name(), c.section())));
        } catch (IOException ignored) {}
    }

    // ---------- TIME SLOTS ----------
    public static ObservableList<TimeSlot> loadTimeSlots() {
        ObservableList<TimeSlot> list = FXCollections.observableArrayList();
        Path p = Path.of(TIMES_CSV);
        if (Files.exists(p)) {
            try (BufferedReader br = Files.newBufferedReader(p)) {
                br.lines().map(l -> l.split(",", -1))
                        .filter(a -> a.length == 2)
                        .map(a -> new TimeSlot(a[0], a[1]))
                        .forEach(list::add);
            } catch (IOException ignored) {}
        }
        return list;
    }

    public static void saveTimeSlots(List<TimeSlot> slots) {
        try (PrintWriter pw = new PrintWriter(TIMES_CSV)) {
            slots.forEach(t -> pw.println(t.from() + "," + t.to()));
        } catch (IOException ignored) {}
    }

    // ---------- SCHEDULED OFFICE HOURS ----------
    public static ObservableList<ScheduledOfficeHour> loadSchedule() {
        ObservableList<ScheduledOfficeHour> list = FXCollections.observableArrayList();
        Path p = Path.of(SCHEDULE_CSV);
        if (Files.exists(p)) {
            try (BufferedReader br = Files.newBufferedReader(p)) {
                br.lines().map(l -> l.split(",", -1))
                        .filter(a -> a.length >= 4)
                        .map(a -> new ScheduledOfficeHour(
                                a[0], a[1], a[2], a[3],
                                a.length > 4 ? a[4] : "",
                                a.length > 5 ? a[5] : ""))
                        .forEach(list::add);
            } catch (IOException ignored) {}
        }
        return list;
    }

    public static void saveSchedule(List<ScheduledOfficeHour> list) {
        try (PrintWriter pw = new PrintWriter(SCHEDULE_CSV)) {
            list.forEach(o -> pw.println(
                    String.join(",", o.student(), o.date(), o.time(),
                            o.course(),  o.reason(), o.comment())));
        } catch (IOException ignored) {}
    }

    // ---------- SEMESTER SETTINGS ----------
    public static Optional<SemesterSettings> loadSemester() {
        Path p = Path.of(SEMESTER_CSV);
        if (!Files.exists(p)) return Optional.empty();
        try (BufferedReader br = Files.newBufferedReader(p)) {
            return br.lines().skip(1).findFirst().map(l -> {
                String[] a = l.split(",", -1);
                Set<String> days = Arrays.stream(a[2].split(";"))
                        .filter(s -> !s.isBlank())
                        .collect(Collectors.toSet());
                return new SemesterSettings(a[0], a[1], days);
            });
        } catch (IOException e) { return Optional.empty(); }
    }

    public static void saveSemester(SemesterSettings s) {
        try (PrintWriter pw = new PrintWriter(SEMESTER_CSV)) {
            pw.println("Semester,Year,Days");
            pw.println(String.join(",", s.semester(), s.year(),
                    String.join(";", s.days())));
        } catch (IOException ignored) {}
    }

    private DataStore() {}
}
