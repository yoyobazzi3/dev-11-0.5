package s25.cs151.application;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class HomePage {
    public static Scene createScene(Stage stage) {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(10, 10, 10, 10));

        Label titleLabel = new Label("Office Hour Manager");
        titleLabel.setStyle("-fx-font-size: 48px;");
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        pane.add(titleLabel, 0, 0, 2, 1);

        // First Name
        Label firstNameLabel = new Label("First Name:");
        firstNameLabel.setStyle("-fx-font-size: 24px;");
        TextField firstName = new TextField();
        firstName.setPrefWidth(250);
        pane.add(firstNameLabel, 0, 1);
        pane.add(firstName, 0, 2);

        // Last Name
        Label lastNameLabel = new Label("Last Name:");
        lastNameLabel.setStyle("-fx-font-size: 24px;");
        TextField lastName = new TextField();
        lastName.setPrefWidth(250);
        pane.add(lastNameLabel, 1, 1);
        pane.add(lastName, 1, 2);

        // Reason
        Label reasonLabel = new Label("Reason:");
        reasonLabel.setStyle("-fx-font-size: 24px;");
        TextField reason = new TextField();
        reason.setPrefWidth(250);
        pane.add(reasonLabel, 0, 3);
        pane.add(reason, 0, 4);

        // Semester Dropdown
        Label semesterLabel = new Label("Semester:");
        semesterLabel.setStyle("-fx-font-size: 24px;");
        ComboBox<String> semesterDropdown = new ComboBox<>();
        semesterDropdown.getItems().addAll("Spring", "Summer", "Fall", "Winter");
        semesterDropdown.setPrefWidth(250);
        pane.add(semesterLabel, 1, 3);
        pane.add(semesterDropdown, 1, 4);

        // Year Dropdown
        Label yearLabel = new Label("Year:");
        yearLabel.setStyle("-fx-font-size: 24px;");
        ComboBox<String> yearDropdown = new ComboBox<>();
        yearDropdown.getItems().addAll("2025", "2026", "2027", "2028");
        yearDropdown.setPrefWidth(250);
        pane.add(yearLabel, 0, 5);
        pane.add(yearDropdown, 0, 6);

        // Day of the Week Dropdown
        Label dayLabel = new Label("Day of the Week:");
        dayLabel.setStyle("-fx-font-size: 24px;");
        ComboBox<String> dayDropdown = new ComboBox<>();
        dayDropdown.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        dayDropdown.setPrefWidth(250);
        pane.add(dayLabel, 1, 5);
        pane.add(dayDropdown, 1, 6);

        // Class Textbox
        Label classLabel = new Label("Class:");
        classLabel.setStyle("-fx-font-size: 24px;");
        TextField classField = new TextField();
        classField.setPrefWidth(250);
        pane.add(classLabel, 0, 7);
        pane.add(classField, 0, 8);

        // Time Slot Dropdown
        Label timeSlotLabel = new Label("Time Slot:");
        timeSlotLabel.setStyle("-fx-font-size: 24px;");
        ComboBox<String> timeSlotDropdown = new ComboBox<>();
        timeSlotDropdown.getItems().addAll("8:00 AM - 9:00 AM", "9:00 AM - 10:00 AM", "10:00 AM - 11:00 AM",
                "11:00 AM - 12:00 PM", "1:00 PM - 2:00 PM", "2:00 PM - 3:00 PM",
                "3:00 PM - 4:00 PM", "4:00 PM - 5:00 PM");
        timeSlotDropdown.setPrefWidth(250);
        pane.add(timeSlotLabel, 1, 7);
        pane.add(timeSlotDropdown, 1, 8);

        // Back Button
        Button backButton = new Button("Back to Landing Page");
        backButton.setOnAction(e -> stage.setScene(LandingPage.createScene(stage)));
        pane.add(backButton, 0, 9);

        return new Scene(pane, 1250, 750);
    }
}