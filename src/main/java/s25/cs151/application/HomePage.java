package s25.cs151.application;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class HomePage {
    private static Model model = new Model();
    private static final String CSV_FILE = "office_hours_data.csv";

    public static Scene createScene(Stage stage) {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10));

        Label titleLabel = new Label("Define Semester's Office Hours");
        titleLabel.setStyle("-fx-font-size: 24px;");
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        pane.add(titleLabel, 0, 0, 2, 1);

        // Semester Dropdown
        ComboBox<String> semesterDropdown = new ComboBox<>();
        semesterDropdown.getItems().addAll("Spring", "Summer", "Fall", "Winter");
        semesterDropdown.setValue("Spring");
        pane.add(new Label("Semester:"), 0, 1);
        pane.add(semesterDropdown, 0, 2);

        // Year Field
        TextField yearField = new TextField();
        yearField.setPromptText("Enter year (e.g., 2025)");
        pane.add(new Label("Year:"), 1, 1);
        pane.add(yearField, 1, 2);

        // Days Checkboxes
        CheckBox[] dayCheckboxes = {
                new CheckBox("Monday"), new CheckBox("Tuesday"),
                new CheckBox("Wednesday"), new CheckBox("Thursday"),
                new CheckBox("Friday")
        };
        GridPane daysPane = new GridPane();
        daysPane.setHgap(10);
        for (int i = 0; i < dayCheckboxes.length; i++) {
            daysPane.add(dayCheckboxes[i], i, 0);
        }
        pane.add(new Label("Days:"), 0, 3);
        pane.add(daysPane, 0, 4, 2, 1);

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            model.setSemester(semesterDropdown.getValue());
            model.setYear(yearField.getText().trim());

            model.clearSelectedDays();
            for (CheckBox cb : dayCheckboxes) {
                if (cb.isSelected()) model.addSelectedDay(cb.getText());
            }

            if (validateAndSave(model)) {
                showAlert("Success", "Data saved successfully!");
                stage.setScene(LandingPage.createScene(stage)); // Return to refreshed landing page
            }
        });

        // Navigation Buttons
        Button backButton = new Button("Back to Landing");
        backButton.setOnAction(e -> stage.setScene(LandingPage.createScene(stage)));

        HBox buttonBox = new HBox(10, saveButton, backButton);
        pane.add(buttonBox, 0, 5, 2, 1);

        return new Scene(pane, 1250, 750);
    }

    private static boolean validateAndSave(Model model) {
        if (model.getYear().isEmpty()) {
            showAlert("Error", "Please enter a year");
            return false;
        }

        try {
            FileWriter writer = new FileWriter(CSV_FILE, true);
            if (new java.io.File(CSV_FILE).length() == 0) {
                writer.write("Semester,Year,Days\n");
            }
            writer.write(String.format("%s,%s,%s\n",
                    model.getSemester(),
                    model.getYear(),
                    String.join(";", model.getSelectedDays())));
            writer.close();
            return true;
        } catch (IOException e) {
            showAlert("Error", "Failed to save data");
            return false;
        }
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}