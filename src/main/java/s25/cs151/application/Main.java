package s25.cs151.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setScene(LandingPage.createScene(stage));
        stage.setTitle("Office Hour Manager");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}