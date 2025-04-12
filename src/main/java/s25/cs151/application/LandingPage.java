package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class LandingPage {

    public static Scene createScene(Stage stage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Office Hours Manager");
        titleLabel.setStyle("-fx-font-size: 24px;");

        // Scheduled Office Hours Table
        TableView<ScheduledEntry> scheduledTableView = new TableView<>();
        setupScheduledTableView(scheduledTableView);
        refreshScheduledData(scheduledTableView);

        // Navigation Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(
                createNavButton("Define Semester", () -> stage.setScene(HomePage.createScene(stage))),
                createNavButton("Define Time Slots", () -> stage.setScene(TimeSlotsPage.createScene(stage))),
                createNavButton("Define Courses", () -> stage.setScene(CoursesPage.createScene(stage))),
                createNavButton("Schedule Office Hours", () -> stage.setScene(ScheduleOfficeHoursPage.createScene(stage))),
                createRefreshButton(scheduledTableView)
        );

        layout.getChildren().addAll(
                titleLabel,
                new Label("\u23F0 Scheduled Office Hours:"),
                scheduledTableView,
                new DatePicker(LocalDate.now()),
                buttonBox
        );

        return new Scene(layout, 1250, 750);
    }

    private static void setupScheduledTableView(TableView<ScheduledEntry> tableView) {
        TableColumn<ScheduledEntry, String> studentCol = new TableColumn<>("Student Name");
        TableColumn<ScheduledEntry, String> dateCol = new TableColumn<>("Date");
        TableColumn<ScheduledEntry, String> timeCol = new TableColumn<>("Time Slot");
        TableColumn<ScheduledEntry, String> courseCol = new TableColumn<>("Course");
        TableColumn<ScheduledEntry, String> reasonCol = new TableColumn<>("Reason");
        TableColumn<ScheduledEntry, String> commentCol = new TableColumn<>("Comment");

        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
        commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));

        tableView.getColumns().setAll(studentCol, dateCol, timeCol, courseCol, reasonCol, commentCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private static void refreshScheduledData(TableView<ScheduledEntry> tableView) {
        ObservableList<ScheduledEntry> data = FXCollections.observableArrayList();
        File file = new File("scheduled_office_hours.csv");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String reason = parts.length > 4 ? parts[4] : "";
                        String comment = parts.length > 5 ? parts[5] : "";
                        data.add(new ScheduledEntry(parts[0], parts[1], parts[2], parts[3], reason, comment));
                    }
                }
            } catch (IOException e) {
                showAlert("Error", "Failed to load scheduled data");
            }
        }

        data.sort(Comparator
                .comparing((ScheduledEntry e) -> LocalDate.parse(e.getDate(), formatter))
                .thenComparing(ScheduledEntry::getTime));

        tableView.setItems(data);
    }

    private static Button createNavButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setOnAction(e -> action.run());
        return button;
    }

    private static Button createRefreshButton(TableView<ScheduledEntry> scheduledTable) {
        Button button = new Button("Refresh");
        button.setOnAction(e -> refreshScheduledData(scheduledTable));
        return button;
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class ScheduledEntry {
        private final String studentName, date, time, course, reason, comment;

        public ScheduledEntry(String studentName, String date, String time, String course, String reason, String comment) {
            this.studentName = studentName;
            this.date = date;
            this.time = time;
            this.course = course;
            this.reason = reason;
            this.comment = comment;
        }

        public String getStudentName() { return studentName; }
        public String getDate() { return date; }
        public String getTime() { return time; }
        public String getCourse() { return course; }
        public String getReason() { return reason; }
        public String getComment() { return comment; }
    }
}
