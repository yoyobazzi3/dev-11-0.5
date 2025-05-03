package s25.cs151.application.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import s25.cs151.application.controller.CourseController;
import s25.cs151.application.model.Course;

public class CoursesView {

    private final Stage stage;
    private final CourseController ctl = new CourseController();

    public CoursesView(Stage s) { stage = s; }

    public Scene getScene() {

        VBox root = new VBox(20); root.setPadding(new Insets(20));

        // form
        GridPane g = new GridPane(); g.setHgap(10); g.setVgap(10);

        TextField code = new TextField();    code.setPromptText("CS151");
        TextField name = new TextField();    name.setPromptText("OOP");
        TextField sec  = new TextField();    sec.setPromptText("01");

        Button add = new Button("Add");
        add.setOnAction(e -> {
            if (ctl.addCourse(new Course(code.getText(), name.getText(), sec.getText()))) {
                code.clear(); name.clear(); sec.clear();
            } else alert("Duplicate entry");
        });

        g.addRow(0, new Label("Code"), code);
        g.addRow(1, new Label("Name"), name);
        g.addRow(2, new Label("Section"), sec);
        g.add(add, 1, 3);

        // table
        TableView<Course> tv = new TableView<>(ctl.getCourses());
        tv.getColumns().addAll(TableUtil.col("Code", Course::code),
                TableUtil.col("Name", Course::name),
                TableUtil.col("Section", Course::section));
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button save = new Button("Save");  save.setOnAction(e -> ctl.save());
        Button back = new Button("Back");  back.setOnAction(e -> stage.setScene(new LandingView(stage).getScene()));

        root.getChildren().addAll(new Label("Courses"), g, tv, new VBox(10, save, back));

        return new Scene(root, 800, 600);
    }

    private void alert(String m) { new Alert(Alert.AlertType.INFORMATION, m).showAndWait(); }
}
