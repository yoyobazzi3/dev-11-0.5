package s25.cs151.application.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import s25.cs151.application.controller.CourseController;
import s25.cs151.application.controller.ScheduleController;
import s25.cs151.application.controller.TimeSlotController;
import s25.cs151.application.model.ScheduledOfficeHour;

import java.time.LocalDate;

public class ScheduleView {

    private final Stage stage;
    private final ScheduleController ctl;

    private final ScheduledOfficeHour editing;

    public ScheduleView(Stage s, ScheduledOfficeHour edit, ScheduleController sharedCtl) {
        stage = s; editing = edit; ctl = sharedCtl; // use shared instance for consistency
    }

    public Scene getScene() {

        CourseController courseCtl = new CourseController();
        TimeSlotController slotCtl = new TimeSlotController();

        VBox root = new VBox(15); root.setPadding(new Insets(20));

        TextField student = new TextField();
        DatePicker date   = new DatePicker(LocalDate.now());
        ComboBox<String> time = new ComboBox<>();
        ComboBox<String> course = new ComboBox<>();
        TextField reason  = new TextField();
        TextField comment = new TextField();

        time.setItems(slotCtl.getSlots().stream()
                .map(Object::toString)
                .collect(FXCollections::observableArrayList,
                        ObservableList::add,
                        ObservableList::addAll));

        course.setItems(courseCtl.getCourses().stream()
                .map(Object::toString)
                .collect(FXCollections::observableArrayList,
                        ObservableList::add,
                        ObservableList::addAll));

        if (editing != null) {
            student.setText(editing.student());
            date.setValue(LocalDate.parse(editing.date()));
            time.setValue(editing.time());
            course.setValue(editing.course());
            reason.setText(editing.reason());
            comment.setText(editing.comment());
        }

        Button save = new Button(editing == null ? "Save" : "Update");
        save.setOnAction(e -> {
            ScheduledOfficeHour entry = new ScheduledOfficeHour(
                    student.getText(), date.getValue().toString(),
                    time.getValue(),   course.getValue(),
                    reason.getText(),  comment.getText());

            boolean ok;
            if (editing == null) {
                ok = ctl.add(entry);
                if (!ok) alert("Conflict detected"); }
            else {
                ctl.replace(editing, entry); ok = true; }

            if (ok) { ctl.save(); stage.setScene(new LandingView(stage).getScene()); }
        });

        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(new LandingView(stage).getScene()));

        root.getChildren().addAll(new Label("Student"), student,
                new Label("Date"),    date,
                new Label("Time"),    time,
                new Label("Course"),  course,
                new Label("Reason"),  reason,
                new Label("Comment"), comment,
                new VBox(10, save, back));

        return new Scene(root, 600, 600);
    }

    private void alert(String m) { new Alert(Alert.AlertType.INFORMATION, m).showAndWait(); }
}
