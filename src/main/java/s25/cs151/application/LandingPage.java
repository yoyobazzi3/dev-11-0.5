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
import java.time.format.DateTimeParseException;
import java.util.Comparator;

public class LandingPage {

    private static ObservableList<ScheduledEntry> allEntries = FXCollections.observableArrayList();
    private static ObservableList<ScheduledEntry> filteredEntries = FXCollections.observableArrayList();
    private static final DateTimeFormatter storageFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Scene createScene(Stage stage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Office Hours Manager");
        titleLabel.setStyle("-fx-font-size: 24px;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search by student name...");

        TableView<ScheduledEntry> scheduledTableView = new TableView<>();
        setupScheduledTableView(scheduledTableView);
        refreshScheduledData(scheduledTableView);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> searchAndUpdate(newVal));

        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> {
            ScheduledEntry selected = scheduledTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                allEntries.remove(selected);
                filteredEntries.remove(selected);
                saveToCSV();
            }
        });

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(
                createNavButton("Define Semester", () -> stage.setScene(DefineSemesterOfficeHours.createScene(stage))),
                createNavButton("Define Time Slots", () -> stage.setScene(TimeSlotsPage.createScene(stage))),
                createNavButton("Define Courses", () -> stage.setScene(CoursesPage.createScene(stage))),
                createNavButton("Schedule Office Hours", () -> stage.setScene(ScheduleOfficeHoursPage.createScene(stage))),
                createRefreshButton(scheduledTableView)
        );

        layout.getChildren().addAll(
                titleLabel,
                searchField,
                new Label("\u23F0 Scheduled Office Hours:"),
                scheduledTableView,
                deleteButton,
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
        tableView.setItems(filteredEntries);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private static void refreshScheduledData(TableView<ScheduledEntry> tableView) {
        allEntries.clear();
        filteredEntries.clear();
        File file = new File("scheduled_office_hours.csv");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 4) {
                        String reason = parts.length > 4 ? parts[4] : "";
                        String comment = parts.length > 5 ? parts[5] : "";
                        // 🔥 Fix time automatically by replacing long dash with normal dash
                        String fixedTime = parts[2].replace("–", "-").trim();
                        allEntries.add(new ScheduledEntry(parts[0], parts[1], fixedTime, parts[3], reason, comment));
                    }
                }
            } catch (IOException e) {
                showAlert("Error", "Failed to load scheduled data");
            }
        }

        searchAndUpdate("");
    }

    private static void searchAndUpdate(String keyword) {
        String lower = keyword.toLowerCase();
        filteredEntries.setAll(
                allEntries.stream()
                        .filter(e -> e.getStudentName().toLowerCase().contains(lower))
                        .sorted(Comparator
                                .comparing((ScheduledEntry e) -> {
                                    try {
                                        return LocalDate.parse(e.getDate(), storageFormatter);
                                    } catch (DateTimeParseException ex) {
                                        return LocalDate.MIN;
                                    }
                                })
                                .reversed()) // ✅ Newest dates first
                        .toList()
        );
    }

    private static void saveToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("scheduled_office_hours.csv"))) {
            for (ScheduledEntry entry : allEntries) {
                writer.println(String.join(",", entry.getStudentName(), entry.getDate(), entry.getTime(),
                        entry.getCourse(), entry.getReason(), entry.getComment()));
            }
        } catch (IOException e) {
            showAlert("Error", "Failed to save data");
        }
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