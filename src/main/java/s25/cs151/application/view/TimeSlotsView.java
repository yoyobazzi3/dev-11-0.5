package s25.cs151.application.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import s25.cs151.application.controller.TimeSlotController;
import s25.cs151.application.model.TimeSlot;

public class TimeSlotsView {

    private final Stage stage;
    private final TimeSlotController ctl = new TimeSlotController();

    public TimeSlotsView(Stage s) { stage = s; }

    public Scene getScene() {

        VBox root = new VBox(20); root.setPadding(new Insets(20));

        // form
        GridPane g = new GridPane(); g.setHgap(10); g.setVgap(10);

        Spinner<Integer> fromH = new Spinner<>(0, 23, 8);
        Spinner<Integer> fromM = new Spinner<>(0, 59, 0);
        Spinner<Integer> toH   = new Spinner<>(0, 23, 9);
        Spinner<Integer> toM   = new Spinner<>(0, 59, 0);

        Button add = new Button("Add");
        add.setOnAction(e -> {
            String f = String.format("%02d:%02d", fromH.getValue(), fromM.getValue());
            String t = String.format("%02d:%02d", toH.getValue(), toM.getValue());
            if (!ctl.addSlot(new TimeSlot(f, t))) alert("Overlap!");
        });

        g.addRow(0, new Label("From"), fromH, new Label(":"), fromM);
        g.addRow(1, new Label("To"),   toH,   new Label(":"), toM);
        g.add(add, 1, 2);

        // table
        TableView<TimeSlot> tv = new TableView<>(ctl.getSlots());
        tv.getColumns().addAll(TableUtil.col("From", TimeSlot::from),
                TableUtil.col("To",   TimeSlot::to));
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button save = new Button("Save"); save.setOnAction(e -> ctl.save());
        Button back = new Button("Back"); back.setOnAction(e -> stage.setScene(new LandingView(stage).getScene()));

        root.getChildren().addAll(new Label("Time Slots"), g, tv, new VBox(10, save, back));
        return new Scene(root, 800, 600);
    }

    private void alert(String m) { new Alert(Alert.AlertType.INFORMATION, m).showAndWait(); }
}
