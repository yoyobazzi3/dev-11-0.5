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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;

public class TimeSlotsPage {
    private static ObservableList<TimeSlot> timeSlots = FXCollections.observableArrayList();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static Scene createScene(Stage stage) {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));

        // Title
        Label titleLabel = new Label("Define Semester's Office Hours");
        titleLabel.setStyle("-fx-font-size: 24px;");

        // Form for adding time slots
        GridPane formPane = new GridPane();
        formPane.setHgap(10);
        formPane.setVgap(10);
        formPane.setPadding(new Insets(10));

        Label fromLabel = new Label("From Hour:");
        Spinner<Integer> fromHourSpinner = new Spinner<>(0, 23, 8);
        Spinner<Integer> fromMinuteSpinner = new Spinner<>(0, 59, 0);
        HBox fromTimeBox = new HBox(5, fromHourSpinner, new Label(":"), fromMinuteSpinner);

        Label toLabel = new Label("To Hour:");
        Spinner<Integer> toHourSpinner = new Spinner<>(0, 23, 9);
        Spinner<Integer> toMinuteSpinner = new Spinner<>(0, 59, 0);
        HBox toTimeBox = new HBox(5, toHourSpinner, new Label(":"), toMinuteSpinner);

        Button addButton = new Button("Add Time Slot");
        addButton.setOnAction(e -> {
            String fromTime = String.format("%02d:%02d", fromHourSpinner.getValue(), fromMinuteSpinner.getValue());
            String toTime = String.format("%02d:%02d", toHourSpinner.getValue(), toMinuteSpinner.getValue());

            try {
                LocalTime start = LocalTime.parse(fromTime, TIME_FORMATTER);
                LocalTime end = LocalTime.parse(toTime, TIME_FORMATTER);

                if (!end.isAfter(start)) {
                    showAlert("Invalid Time", "End time must be after start time.");
                    return;
                }

                // Check for overlapping time slots
                for (TimeSlot slot : timeSlots) {
                    LocalTime existingStart = LocalTime.parse(slot.getFromTime(), TIME_FORMATTER);
                    LocalTime existingEnd = LocalTime.parse(slot.getToTime(), TIME_FORMATTER);

                    if ((start.isBefore(existingEnd) && end.isAfter(existingStart))) {
                        showAlert("Overlap Detected", "This time slot overlaps with an existing one.");
                        return;
                    }
                }

                timeSlots.add(new TimeSlot(fromTime, toTime));
                timeSlots.sort(Comparator.comparing(slot -> LocalTime.parse(slot.getFromTime(), TIME_FORMATTER)));

            } catch (DateTimeParseException ex) {
                showAlert("Invalid Time", "Please enter valid times in HH:MM format.");
            }
        });

        formPane.add(fromLabel, 0, 0);
        formPane.add(fromTimeBox, 1, 0);
        formPane.add(toLabel, 0, 1);
        formPane.add(toTimeBox, 1, 1);
        formPane.add(addButton, 0, 2, 2, 1);

        // Table to display time slots
        TableView<TimeSlot> tableView = new TableView<>();
        tableView.setItems(timeSlots);

        TableColumn<TimeSlot, String> fromCol = new TableColumn<>("From Hour");
        fromCol.setCellValueFactory(new PropertyValueFactory<>("fromTime"));

        TableColumn<TimeSlot, String> toCol = new TableColumn<>("To Hour");
        toCol.setCellValueFactory(new PropertyValueFactory<>("toTime"));

        tableView.getColumns().addAll(fromCol, toCol);

        // Save button
        Button saveButton = new Button("Save All Time Slots");
        saveButton.setOnAction(e -> saveTimeSlots());

        // Navigation buttons
        Button backButton = new Button("Back to Landing Page");
        backButton.setOnAction(e -> stage.setScene(LandingPage.createScene(stage)));

        HBox buttonBox = new HBox(10, saveButton, backButton);

        // Load existing time slots
        loadTimeSlots();
        timeSlots.sort(Comparator.comparing(slot -> LocalTime.parse(slot.getFromTime(), TIME_FORMATTER)));

        mainLayout.getChildren().addAll(titleLabel, formPane, tableView, buttonBox);
        return new Scene(mainLayout, 1250, 750);
    }

    private static void saveTimeSlots() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("time_slots.csv"))) {
            for (TimeSlot slot : timeSlots) {
                writer.println(slot.getFromTime() + "," + slot.getToTime());
            }
            showAlert("Success", "Time slots saved successfully!");
        } catch (IOException e) {
            showAlert("Error", "Failed to save time slots.");
            e.printStackTrace();
        }
    }

    private static void loadTimeSlots() {
        timeSlots.clear();
        File file = new File("time_slots.csv");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        timeSlots.add(new TimeSlot(parts[0], parts[1]));
                    }
                }
            } catch (IOException e) {
                showAlert("Error", "Failed to load time slots.");
                e.printStackTrace();
            }
        }
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class TimeSlot {
        private final String fromTime;
        private final String toTime;

        public TimeSlot(String fromTime, String toTime) {
            this.fromTime = fromTime;
            this.toTime = toTime;
        }

        public String getFromTime() {
            return fromTime;
        }

        public String getToTime() {
            return toTime;
        }
    }
}
