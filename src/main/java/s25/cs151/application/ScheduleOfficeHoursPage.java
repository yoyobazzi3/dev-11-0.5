package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.function.Consumer;

public class ScheduleOfficeHoursPage {
    private static ObservableList<ScheduledOfficeHour> entries = FXCollections.observableArrayList();

    public static Scene createScene(Stage stage) {
        return createScene(stage, null, null);
    }

    public static Scene createScene(Stage stage, ScheduledOfficeHour existingEntry, Runnable onSaveCallback) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        Label title = new Label(existingEntry == null ? "Schedule Office Hours" : "Edit Office Hours");
        title.setStyle("-fx-font-size: 24px;");

        TextField studentNameField = new TextField();
        studentNameField.setPromptText("Enter student's full name");
        if (existingEntry != null) studentNameField.setText(existingEntry.getStudentName());

        DatePicker datePicker = new DatePicker(LocalDate.now());
        if (existingEntry != null) datePicker.setValue(LocalDate.parse(existingEntry.getDate()));

        ComboBox<String> timeSlotDropdown = new ComboBox<>();
        timeSlotDropdown.setPromptText("Select a time slot");
        loadTimeSlots(timeSlotDropdown);
        if (existingEntry != null) timeSlotDropdown.setValue(existingEntry.getTime());

        ComboBox<String> courseDropdown = new ComboBox<>();
        courseDropdown.setPromptText("Select a course");
        loadCourses(courseDropdown);
        if (existingEntry != null) courseDropdown.setValue(existingEntry.getCourse());

        TextField reasonField = new TextField();
        reasonField.setPromptText("Reason (optional)");
        if (existingEntry != null) reasonField.setText(existingEntry.getReason());

        TextField commentField = new TextField();
        commentField.setPromptText("Comment (optional)");
        if (existingEntry != null) commentField.setText(existingEntry.getComment());

        Button saveBtn = new Button(existingEntry == null ? "Save Schedule" : "Save Changes");
        saveBtn.setOnAction(e -> {
            String studentName = studentNameField.getText().trim();
            LocalDate date = datePicker.getValue();
            String time = timeSlotDropdown.getValue();
            String course = courseDropdown.getValue();
            String reason = reasonField.getText().trim();
            String comment = commentField.getText().trim();

            if (studentName.isEmpty() || date == null || time == null || course == null) {
                showAlert("Missing Fields", "Please fill in all required fields.");
                return;
            }

            if (existingEntry == null && hasConflict(date.toString(), time, course)) {
                showAlert("Conflict Detected", "This course already has office hours scheduled at that time and date.");
                return;
            }

            ScheduledOfficeHour newEntry = new ScheduledOfficeHour(studentName, date.toString(), time, course, reason, comment);

            if (existingEntry != null) {
                File file = new File("scheduled_office_hours.csv");
                File temp = new File("temp.csv");

                try (
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        PrintWriter writer = new PrintWriter(new FileWriter(temp))
                ) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",", -1);
                        if (parts.length >= 4 &&
                                !(parts[0].equals(existingEntry.getStudentName()) &&
                                        parts[1].equals(existingEntry.getDate()) &&
                                        parts[2].equals(existingEntry.getTime()) &&
                                        parts[3].equals(existingEntry.getCourse()))) {
                            writer.println(line);
                        }
                    }
                    writer.println(String.join(",", studentName, date.toString(), time, course, reason, comment));
                } catch (IOException ex) {
                    showAlert("Error", "Failed to save edited schedule.");
                }

                file.delete();
                temp.renameTo(file);
                showAlert("Success", "Schedule updated!");
            } else {
                entries.add(newEntry);
                saveLatestSchedule(newEntry);
                showAlert("Success", "Office hour scheduled successfully!");
            }

            entries.clear();
            if (onSaveCallback != null) onSaveCallback.run();
            stage.setScene(LandingPage.createScene(stage));
        });

        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(LandingPage.createScene(stage)));

        layout.getChildren().addAll(
                title,
                new Label("Student Name:"), studentNameField,
                new Label("Date:"), datePicker,
                new Label("Time Slot:"), timeSlotDropdown,
                new Label("Course:"), courseDropdown,
                new Label("Reason:"), reasonField,
                new Label("Comment:"), commentField,
                saveBtn,
                back
        );

        loadSchedule();

        return new Scene(layout, 1250, 750);
    }

    private static void loadCourses(ComboBox<String> comboBox) {
        File file = new File("courses.csv");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        comboBox.getItems().add(parts[0] + "-" + parts[2]);
                    }
                }
            } catch (IOException e) {
                showAlert("Error", "Failed to load courses");
            }
        }
    }

    private static void loadTimeSlots(ComboBox<String> comboBox) {
        File file = new File("time_slots.csv");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        comboBox.getItems().add(parts[0] + " – " + parts[1]);
                    }
                }
            } catch (IOException e) {
                showAlert("Error", "Failed to load time slots");
            }
        }
    }

    private static void saveLatestSchedule(ScheduledOfficeHour latest) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("scheduled_office_hours.csv", true))) {
            writer.println(latest.getStudentName() + "," + latest.getDate() + "," + latest.getTime() + "," +
                    latest.getCourse() + "," + latest.getReason() + "," + latest.getComment());
        } catch (IOException e) {
            showAlert("Error", "Failed to save schedule");
        }
    }

    private static void loadSchedule() {
        entries.clear();
        File file = new File("scheduled_office_hours.csv");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 4) {
                        String reason = parts.length > 4 ? parts[4] : "";
                        String comment = parts.length > 5 ? parts[5] : "";
                        entries.add(new ScheduledOfficeHour(parts[0], parts[1], parts[2], parts[3], reason, comment));
                    }
                }
            } catch (IOException e) {
                showAlert("Error", "Failed to load schedule");
            }
        }
    }

    private static boolean hasConflict(String date, String time, String course) {
        return entries.stream().anyMatch(entry ->
                entry.getDate().equals(date) &&
                        entry.getTime().equals(time) &&
                        entry.getCourse().equals(course)
        );
    }

    private static void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class ScheduledOfficeHour {
        private final String studentName, date, time, course, reason, comment;

        public ScheduledOfficeHour(String studentName, String date, String time, String course, String reason, String comment) {
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
