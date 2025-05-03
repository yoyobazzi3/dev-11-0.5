package s25.cs151.application.view;

import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import s25.cs151.application.controller.*;
import s25.cs151.application.model.ScheduledOfficeHour;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LandingView {

    private final Stage stage;
    private final ScheduleController scheduleCtl = new ScheduleController();
    private final FilteredList<ScheduledOfficeHour> filtered =
            new FilteredList<>(scheduleCtl.getEntries(), p -> true);

    public LandingView(Stage st) { this.stage = st; }

    public Scene getScene() {

        VBox root = new VBox(20); root.setPadding(new Insets(20));

        // Search
        TextField search = new TextField();
        search.setPromptText("search…");
        search.textProperty().addListener((o, ov, nv) ->
                filtered.setPredicate(e -> nv.isBlank()
                        || e.student().toLowerCase().contains(nv.toLowerCase())
                        || e.course().toLowerCase().contains(nv.toLowerCase())));

        // Table
        TableView<ScheduledOfficeHour> tv = new TableView<>(filtered);
        tv.getColumns().addAll(TableUtil.col("Student",  ScheduledOfficeHour::student),
                TableUtil.col("Date",     ScheduledOfficeHour::date),
                TableUtil.col("Time",     ScheduledOfficeHour::time),
                TableUtil.col("Course",   ScheduledOfficeHour::course),
                TableUtil.col("Reason",   ScheduledOfficeHour::reason),
                TableUtil.col("Comment",  ScheduledOfficeHour::comment));
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Delete / Edit
        Button del = new Button("Delete");
        del.setOnAction(e -> {
            var sel = tv.getSelectionModel().getSelectedItem();
            if (sel != null) { scheduleCtl.remove(sel); scheduleCtl.save(); }
        });

        Button edit = new Button("Edit");
        edit.setOnAction(e -> {
            var sel = tv.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            stage.setScene(new ScheduleView(stage, sel, scheduleCtl).getScene());
        });

        // Nav
        Button semBtn = new Button("Semester");
        semBtn.setOnAction(e -> stage.setScene(new SemesterView(stage).getScene()));

        Button slotBtn = new Button("Time Slots");
        slotBtn.setOnAction(e -> stage.setScene(new TimeSlotsView(stage).getScene()));

        Button courseBtn = new Button("Courses");
        courseBtn.setOnAction(e -> stage.setScene(new CoursesView(stage).getScene()));

        Button schedBtn = new Button("Schedule");
        schedBtn.setOnAction(e -> stage.setScene(new ScheduleView(stage, null, scheduleCtl).getScene()));

        root.getChildren().addAll(new Label("Office Hours Manager"),
                search, tv,
                new HBox(10, del, edit),
                new HBox(10, semBtn, slotBtn, courseBtn, schedBtn));

        return new Scene(root, 1250, 750);
    }
}
