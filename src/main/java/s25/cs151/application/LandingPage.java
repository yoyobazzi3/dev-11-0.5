package s25.cs151.application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;

public class LandingPage {
    public static Scene createScene(Stage stage) {
        VBox officeHoursContainer = new VBox(10);
        officeHoursContainer.setPadding(new Insets(10));
        officeHoursContainer.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: white;");
        Label officeHoursLabel = new Label("Today's Meetings");
        TableView<String> officeHoursTable = new TableView<>();
        officeHoursContainer.getChildren().addAll(officeHoursLabel, officeHoursTable);

        DatePicker datePicker = new DatePicker(LocalDate.now());

        Button homeButton = new Button("Go to Homepage");
        homeButton.setOnAction(e -> stage.setScene(HomePage.createScene(stage)));

        VBox layout = new VBox(20, officeHoursContainer, datePicker, homeButton);
        layout.setPadding(new Insets(20));

        return new Scene(layout, 1250, 750);
    }
}