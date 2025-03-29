package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;

public class LandingPage {
    public static Scene createScene(Stage stage) {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Office Hours Manager");
        titleLabel.setStyle("-fx-font-size: 24px;");

        // Table Setup
        TableView<OfficeHoursEntry> tableView = new TableView<>();
        setupTableView(tableView);
        refreshTableData(tableView);

        // Navigation Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(
                createNavButton("Define Semester", () -> stage.setScene(HomePage.createScene(stage))),
                createNavButton("Define Time Slots", () -> stage.setScene(TimeSlotsPage.createScene(stage))),
                createNavButton("Define Courses", () -> stage.setScene(CoursesPage.createScene(stage))),
                createRefreshButton(tableView)
        );

        layout.getChildren().addAll(titleLabel, tableView, new DatePicker(LocalDate.now()), buttonBox);
        return new Scene(layout, 1250, 750);
    }

    private static void setupTableView(TableView<OfficeHoursEntry> tableView) {
        TableColumn<OfficeHoursEntry, String> semesterCol = new TableColumn<>("Semester");
        TableColumn<OfficeHoursEntry, String> yearCol = new TableColumn<>("Year");
        TableColumn<OfficeHoursEntry, String> daysCol = new TableColumn<>("Days");

        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        daysCol.setCellValueFactory(new PropertyValueFactory<>("days"));

        tableView.getColumns().setAll(semesterCol, yearCol, daysCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // This section sorts the tables
        yearCol.setSortable(true);
        // semesterCol.setSortable(true);

        yearCol.setSortType(TableColumn.SortType.DESCENDING);
        // semesterCol.setSortType(TableColumn.SortType.DESCENDING);

        tableView.getSortOrder().add(yearCol);
        // tableView.getSortOrder().add(semesterCol);
    }

    private static void refreshTableData(TableView<OfficeHoursEntry> tableView) {
        ObservableList<OfficeHoursEntry> data = FXCollections.observableArrayList();
        File file = new File("office_hours_data.csv");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean headerSkipped = false;
                while ((line = reader.readLine()) != null) {
                    if (!headerSkipped) {
                        headerSkipped = true;
                        continue;
                    }
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        data.add(new OfficeHoursEntry(
                                parts[0],
                                parts[1],
                                parts[2].replace(";", ", ")
                        ));
                    }
                }
            } catch (IOException e) {
                showAlert("Error", "Failed to load data");
            }
        }

        // Define the custom order for semesters
        Comparator<String> semesterOrder = Comparator
                .comparingInt(s -> {
                    switch (s) {
                        case "Winter": return 0;
                        case "Fall": return 1;
                        case "Summer": return 2;
                        case "Spring": return 3;
                        default: return 4; // Handle unexpected values
                    }
                });

        // Sort data by year (descending) and then by semester (custom order)
        data.sort(Comparator
                .comparing(OfficeHoursEntry::getYear, Comparator.reverseOrder())
                .thenComparing(OfficeHoursEntry::getSemester, semesterOrder)
        );

        tableView.setItems(data);
        tableView.refresh();
    }

    private static Button createNavButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setOnAction(e -> action.run());
        return button;
    }

    private static Button createRefreshButton(TableView<OfficeHoursEntry> tableView) {
        Button button = new Button("Refresh");
        button.setOnAction(e -> refreshTableData(tableView));
        return button;
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class OfficeHoursEntry {
        private final String semester;
        private final String year;
        private final String days;

        public OfficeHoursEntry(String semester, String year, String days) {
            this.semester = semester;
            this.year = year;
            this.days = days;
        }

        public String getSemester() { return semester; }
        public String getYear() { return year; }
        public String getDays() { return days; }
    }
}
