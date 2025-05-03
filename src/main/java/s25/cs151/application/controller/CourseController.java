package s25.cs151.application.controller;

import javafx.collections.ObservableList;
import s25.cs151.application.model.Course;
import s25.cs151.application.model.DataStore;

public class CourseController {
    private final ObservableList<Course> courses = DataStore.loadCourses();

    public ObservableList<Course> getCourses()  { return courses; }

    public boolean addCourse(Course c) {
        boolean exists = courses.stream()
                .anyMatch(e -> e.code().equalsIgnoreCase(c.code())
                        && e.section().equalsIgnoreCase(c.section()));
        if (!exists) courses.add(c);
        return !exists;
    }

    public void save() { DataStore.saveCourses(courses); }
}
