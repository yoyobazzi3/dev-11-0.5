package s25.cs151.application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        // Office hours container
        VBox officeHoursContainer = new VBox(10);
        officeHoursContainer.setPadding(new Insets(10));
        officeHoursContainer.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: white;");
        Label officeHoursLabel = new Label("Today's Meetings");
        TableView<String> officeHoursTable = new TableView<>(); // Placeholder table
        officeHoursContainer.getChildren().addAll(officeHoursLabel, officeHoursTable);

        // Date Picker (Calendar)
        DatePicker datePicker = new DatePicker(LocalDate.now());

        // Navigation Button
        Button homeButton = new Button("Go to Homepage");
        homeButton.setOnAction(e -> goToHomePage(stage));

        // Layout
        VBox layout = new VBox(20, officeHoursContainer, datePicker, homeButton);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 600, 400);
        stage.setTitle("Landing Page");
        stage.setScene(scene);
        stage.show();
    }

    private void goToHomePage(Stage stage) {
        // Placeholder for homepage navigation
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Navigating to Homepage...");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
