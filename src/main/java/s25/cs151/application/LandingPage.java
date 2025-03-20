package s25.cs151.application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class LandingPage {
    public static Scene createScene(Stage stage) {
        // Main container
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        // Title
        Label titleLabel = new Label("Office Hour Manager - Landing Page");
        titleLabel.setStyle("-fx-font-size: 24px;");

        // Container for saved data
        VBox savedDataContainer = new VBox(10);
        savedDataContainer.setPadding(new Insets(15));

        // Header for saved data
        Label savedDataLabel = new Label("Saved Office Hours Data");
        savedDataLabel.setStyle("-fx-font-size: 18px;");

        // Read and display CSV data
        VBox csvDataBox = new VBox(5);
        try (BufferedReader reader = new BufferedReader(new FileReader("office_hours_data.csv"))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    // Skip the header line
                    isHeader = false;
                    continue;
                }
                // Split CSV line into components
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String semester = parts[0];
                    String year = parts[1];
                    String days = parts[2].replace(";", ", ");

                    // Create a label for each entry (concise format)
                    Label entryLabel = new Label(String.format("%s | %s | %s", semester, year, days));
                    entryLabel.setStyle("-fx-font-size: 16px;");
                    csvDataBox.getChildren().add(entryLabel);
                }
            }
        } catch (IOException e) {
            Label errorLabel = new Label("Failed to read CSV file.");
            errorLabel.setStyle("-fx-font-size: 16px;");
            csvDataBox.getChildren().add(errorLabel);
        }

        // Add components to the saved data container
        savedDataContainer.getChildren().addAll(savedDataLabel, csvDataBox);

        // Date Picker
        DatePicker datePicker = new DatePicker(LocalDate.now());

        // Home Button
        Button homeButton = new Button("Go to Homepage");
        homeButton.setOnAction(e -> stage.setScene(HomePage.createScene(stage)));

        // Add all components to the main layout
        layout.getChildren().addAll(titleLabel, savedDataContainer, datePicker, homeButton);

        return new Scene(layout, 1250, 750);
    }
}