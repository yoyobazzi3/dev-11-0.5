//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package s25.cs151.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import s25.cs151.application.view.LandingView;

public class Main extends Application {
    public Main() {
    }

    public void start(Stage stage) {
        Scene first = (new LandingView(stage)).getScene();
        stage.setScene(first);
        stage.setTitle("Office Hours Manager");
        stage.show();
    }

    public static void main(String[] args) {
        launch(new String[0]);
    }
}
