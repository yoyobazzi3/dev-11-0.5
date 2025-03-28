package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class CoursesPage {
    private static ObservableList<Course> courses = FXCollections.observableArrayList();
    private static Set<String> courseKeys = new HashSet<>();

    public static Scene createScene(Stage stage) {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));

        // Title
        Label titleLabel = new Label("Define Courses");
        titleLabel.setStyle("-fx-font-size: 24px;");

        // Form for adding courses
        GridPane formPane = new GridPane();
        formPane.setHgap(10);
        formPane.setVgap(10);
        formPane.setPadding(new Insets(10));

        Label codeLabel = new Label("Course Code:");
        TextField codeField = new TextField();
        codeField.setPromptText("e.g., CS151");

        Label nameLabel = new Label("Course Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Object-Oriented Design");

        Label sectionLabel = new Label("Section Number:");
        TextField sectionField = new TextField();
        sectionField.setPromptText("e.g., 01");

        Button addButton = new Button("Add Course");
        addButton.setOnAction(e -> {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String section = sectionField.getText().trim();

            if (code.isEmpty() || name.isEmpty() || section.isEmpty()) {
                showAlert("Error", "All fields are required.");
                return;
            }

            String key = code + "-" + name + "-" + section;
            if (courseKeys.contains(key)) {
                showAlert("Error", "This course combination already exists.");
                return;
            }

            courses.add(new Course(code, name, section));
            courseKeys.add(key);
            codeField.clear();
            nameField.clear();
            sectionField.clear();
        });

        formPane.add(codeLabel, 0, 0);
        formPane.add(codeField, 1, 0);
        formPane.add(nameLabel, 0, 1);
        formPane.add(nameField, 1, 1);
        formPane.add(sectionLabel, 0, 2);
        formPane.add(sectionField, 1, 2);
        formPane.add(addButton, 0, 3, 2, 1);

        // Table to display courses
        TableView<Course> tableView = new TableView<>();
        tableView.setItems(courses);

        TableColumn<Course, String> codeCol = new TableColumn<>("Course Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        codeCol.setSortType(TableColumn.SortType.DESCENDING);

        TableColumn<Course, String> nameCol = new TableColumn<>("Course Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Course, String> sectionCol = new TableColumn<>("Section Number");
        sectionCol.setCellValueFactory(new PropertyValueFactory<>("section"));

        tableView.getColumns().addAll(codeCol, nameCol, sectionCol);
        tableView.getSortOrder().add(codeCol);

        // Save button
        Button saveButton = new Button("Save All Courses");
        saveButton.setOnAction(e -> saveCourses());

        // Navigation buttons
        Button backButton = new Button("Back to Landing Page");
        backButton.setOnAction(e -> stage.setScene(LandingPage.createScene(stage)));

        HBox buttonBox = new HBox(10, saveButton, backButton);

        // Load existing courses
        loadCourses();

        mainLayout.getChildren().addAll(titleLabel, formPane, tableView, buttonBox);
        return new Scene(mainLayout, 1250, 750);
    }

    private static void saveCourses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("courses.csv"))) {
            for (Course course : courses) {
                writer.println(course.getCode() + "," + course.getName() + "," + course.getSection());
            }
            showAlert("Success", "Courses saved successfully!");
        } catch (IOException e) {
            showAlert("Error", "Failed to save courses.");
            e.printStackTrace();
        }
    }

    private static void loadCourses() {
        courses.clear();
        courseKeys.clear();
        File file = new File("courses.csv");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String code = parts[0];
                        String name = parts[1];
                        String section = parts[2];
                        courses.add(new Course(code, name, section));
                        courseKeys.add(code + "-" + name + "-" + section);
                    }
                }
            } catch (IOException e) {
                showAlert("Error", "Failed to load courses.");
                e.printStackTrace();
            }
        }
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Course {
        private final String code;
        private final String name;
        private final String section;

        public Course(String code, String name, String section) {
            this.code = code;
            this.name = name;
            this.section = section;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getSection() {
            return section;
        }
    }
}