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

        Label firstNameLabel = new Label("First Name:");
        firstNameLabel.setStyle("-fx-font-size: 24px;");
        TextField firstName = new TextField();
        firstName.setPrefWidth(250);
        pane.add(firstNameLabel, 0, 1);
        pane.add(firstName, 0, 2);

        Label lastNameLabel = new Label("Last Name:");
        lastNameLabel.setStyle("-fx-font-size: 24px;");
        TextField lastName = new TextField();
        lastName.setPrefWidth(250);
        pane.add(lastNameLabel, 1, 1);
        pane.add(lastName, 1, 2);

        Label reasonLabel = new Label("Reason:");
        reasonLabel.setStyle("-fx-font-size: 24px;");
        TextField reason = new TextField();
        reason.setPrefWidth(250);
        pane.add(reasonLabel, 0, 3);
        pane.add(reason, 0, 4);

        Button backButton = new Button("Back to Landing Page");
        backButton.setOnAction(e -> stage.setScene(LandingPage.createScene(stage)));
        pane.add(backButton, 0, 5);

        return new Scene(pane, 1250, 750);
    }
}