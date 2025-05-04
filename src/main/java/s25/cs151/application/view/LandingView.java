package s25.cs151.application.view;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import s25.cs151.application.controller.ScheduleController;
import s25.cs151.application.model.ScheduledOfficeHour;

public class LandingView {

    private final Stage stage;
    private final ScheduleController scheduleCtl = new ScheduleController();
    private final FilteredList<ScheduledOfficeHour> filtered =
            new FilteredList<>(scheduleCtl.getEntries(), p -> true);

    public LandingView(Stage st) { this.stage = st; }

    public Scene getScene() {

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        /* ── Search ── */
        TextField search = new TextField();
        search.setPromptText("search…");
        search.textProperty().addListener((o, ov, nv) ->
                filtered.setPredicate(e -> nv.isBlank()
                        || e.student().toLowerCase().contains(nv.toLowerCase())
                        || e.course().toLowerCase().contains(nv.toLowerCase()))
        );

        /* ── Table ── */
        TableView<ScheduledOfficeHour> tv = new TableView<>();
        TableColumn<ScheduledOfficeHour, ?> studentCol = TableUtil.col("Student",  ScheduledOfficeHour::student);
        TableColumn<ScheduledOfficeHour, ?> dateCol    = TableUtil.col("Date",     ScheduledOfficeHour::date);
        TableColumn<ScheduledOfficeHour, ?> timeCol    = TableUtil.col("Time",     ScheduledOfficeHour::time);
        TableColumn<ScheduledOfficeHour, ?> courseCol  = TableUtil.col("Course",   ScheduledOfficeHour::course);
        TableColumn<ScheduledOfficeHour, ?> reasonCol  = TableUtil.col("Reason",   ScheduledOfficeHour::reason);
        TableColumn<ScheduledOfficeHour, ?> commentCol = TableUtil.col("Comment",  ScheduledOfficeHour::comment);

        tv.getColumns().addAll(studentCol, dateCol, timeCol, courseCol, reasonCol, commentCol);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        SortedList<ScheduledOfficeHour> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tv.comparatorProperty());
        tv.setItems(sorted);

        /* default sort: newest date, then latest time */
        dateCol.setSortType(TableColumn.SortType.DESCENDING);
        timeCol.setSortType(TableColumn.SortType.DESCENDING);
        tv.getSortOrder().addAll(dateCol, timeCol);
        tv.sort();

        /* ── Buttons (unchanged) ── */
        Button del  = new Button("Delete");
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

        Button semBtn   = new Button("Semester");
        semBtn.setOnAction(e -> stage.setScene(new SemesterView(stage).getScene()));
        Button slotBtn  = new Button("Time Slots");
        slotBtn.setOnAction(e -> stage.setScene(new TimeSlotsView(stage).getScene()));
        Button courseBtn = new Button("Courses");
        courseBtn.setOnAction(e -> stage.setScene(new CoursesView(stage).getScene()));
        Button schedBtn = new Button("Schedule");
        schedBtn.setOnAction(e -> stage.setScene(new ScheduleView(stage, null, scheduleCtl).getScene()));

        root.getChildren().addAll(
                new Label("Office Hours Manager"),
                search,
                tv,
                new HBox(10, del, edit),
                new HBox(10, semBtn, slotBtn, courseBtn, schedBtn)
        );

        return new Scene(root, 1250, 750);
    }
}
