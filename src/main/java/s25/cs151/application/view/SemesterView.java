package s25.cs151.application.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import s25.cs151.application.controller.SemesterController;
import s25.cs151.application.model.SemesterSettings;

import java.util.HashSet;
import java.util.Set;

public class SemesterView {

    private final Stage stage;
    private final SemesterController ctl = new SemesterController();

    public SemesterView(Stage s) { stage = s; }

    public Scene getScene() {

        VBox root = new VBox(20); root.setPadding(new Insets(20));

        GridPane g = new GridPane(); g.setHgap(10); g.setVgap(10);

        ComboBox<String> sem = new ComboBox<>();
        sem.getItems().addAll("Spring", "Summer", "Fall", "Winter");
        sem.setValue("Spring");

        TextField yr = new TextField();

        CheckBox mon = new CheckBox("Mon"); CheckBox tue = new CheckBox("Tue");
        CheckBox wed = new CheckBox("Wed"); CheckBox thu = new CheckBox("Thu");
        CheckBox fri = new CheckBox("Fri");

        ctl.getSettings().ifPresent(sv -> {
            sem.setValue(sv.semester());
            yr.setText(sv.year());
            sv.days().forEach(d -> {
                switch (d) {
                    case "Monday" -> mon.setSelected(true);
                    case "Tuesday" -> tue.setSelected(true);
                    case "Wednesday" -> wed.setSelected(true);
                    case "Thursday" -> thu.setSelected(true);
                    case "Friday" -> fri.setSelected(true);
                }
            });
        });

        g.addRow(0, new Label("Semester"), sem);
        g.addRow(1, new Label("Year"),     yr);
        g.addRow(2, mon, tue, wed, thu, fri);

        Button save = new Button("Save");
        save.setOnAction(e -> {
            Set<String> d = new HashSet<>();
            if (mon.isSelected()) d.add("Monday");
            if (tue.isSelected()) d.add("Tuesday");
            if (wed.isSelected()) d.add("Wednesday");
            if (thu.isSelected()) d.add("Thursday");
            if (fri.isSelected()) d.add("Friday");
            ctl.save(new SemesterSettings(sem.getValue(), yr.getText(), d));
        });

        Button back = new Button("Back");
        back.setOnAction(e -> stage.setScene(new LandingView(stage).getScene()));

        root.getChildren().addAll(new Label("Semester Settings"), g, new HBox(10, save, back));
        return new Scene(root, 600, 400);
    }
}
