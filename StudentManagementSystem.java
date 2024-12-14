import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StudentManagementSystem extends Application {


    private TableView<Student> studentTable = new TableView<>();
    private TableView<Subject> subjectTable = new TableView<>();
    private TableView<AttendanceRecord> attendanceTable = new TableView<>();


    private ObservableList<Student> students = FXCollections.observableArrayList();
    private ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private ObservableList<AttendanceRecord> attendanceRecords = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Создание интерфейса
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Верхняя панель с кнопками
        HBox topPanel = new HBox(10);
        Button addStudentBtn = new Button("Добавить студента");
        Button addSubjectBtn = new Button("Добавить предмет");
        topPanel.getChildren().addAll(addStudentBtn, addSubjectBtn);

        // Настройка таблицы студентов
        setupStudentTable();

        // Настройка таблицы предметов
        setupSubjectTable();

        // Настройка таблицы посещаемости
        setupAttendanceTable();

        // Обработчики кнопок
        addStudentBtn.setOnAction(e -> showAddStudentDialog());
        addSubjectBtn.setOnAction(e -> showAddSubjectDialog());

        // Компоновка элементов
        HBox tablesPanel = new HBox(10);
        tablesPanel.getChildren().addAll(
            new VBox(5, new Label("Студенты"), studentTable),
            new VBox(5, new Label("Предметы"), subjectTable)
        );

        root.getChildren().addAll(
            topPanel,
            tablesPanel,
            new VBox(5, new Label("Посещаемость"), attendanceTable)
        );

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Система учета студентов");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static class Student {
        private final int id;
        private final String name;
        private final String groupName;

        public Student(int id, String name, String groupName) {
            this.id = id;
            this.name = name;
            this.groupName = groupName;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getGroupName() { return groupName; }
    }

    public static class Subject {
        private final int id;
        private final String name;

        public Subject(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }
        public String getName() { return name; }
    }

    public static class AttendanceRecord {
        private final int studentId;
        private final String studentName;
        private final Subject subject;
        private boolean isPresent;
        private Integer grade;

        public AttendanceRecord(int studentId, String studentName, Subject subject) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.subject = subject;
            this.isPresent = true;
        }

        public String getStudentName() { return studentName; }
        public Subject getSubject() { return subject; }
        public boolean isPresent() { return isPresent; }
        public Integer getGrade() { return grade; }
        public void setPresent(boolean present) { isPresent = present; }
        public void setGrade(Integer grade) { this.grade = grade; }
    }

    // Настройка таблиц
    private void setupStudentTable() {
        TableColumn<Student, String> nameCol = new TableColumn<>("ФИО");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, String> groupCol = new TableColumn<>("Группа");
        groupCol.setCellValueFactory(new PropertyValueFactory<>("groupName"));

        studentTable.getColumns().addAll(nameCol, groupCol);
        studentTable.setItems(students);
    }

    private void setupSubjectTable() {
        TableColumn<Subject, String> nameCol = new TableColumn<>("Предмет");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        subjectTable.getColumns().add(nameCol);
        subjectTable.setItems(subjects);
    }

    private void setupAttendanceTable() {
        TableColumn<AttendanceRecord, String> nameCol = new TableColumn<>("Студент");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        TableColumn<AttendanceRecord, Subject> subjectCol = new TableColumn<>("Предмет");
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        subjectCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Subject item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        TableColumn<AttendanceRecord, Boolean> presentCol = new TableColumn<>("Присутствие");
        presentCol.setCellValueFactory(new PropertyValueFactory<>("present"));
        presentCol.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();
            {
                checkBox.setOnAction(event -> {
                    AttendanceRecord record = getTableRow().getItem();
                    if (record != null) {
                        record.setPresent(checkBox.isSelected());
                    }
                });
            }
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    AttendanceRecord record = getTableRow().getItem();
                    if (record != null) {
                        checkBox.setSelected(record.isPresent());
                    }
                    setGraphic(checkBox);
                }
            }
        });

        TableColumn<AttendanceRecord, Integer> gradeCol = new TableColumn<>("Оценка");
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeCol.setCellFactory(col -> new TableCell<>() {
            private final ComboBox<Integer> comboBox = new ComboBox<>(
                FXCollections.observableArrayList(2, 3, 4, 5)
            );
            {
                comboBox.setOnAction(event -> {
                    AttendanceRecord record = getTableRow().getItem();
                    if (record != null) {
                        record.setGrade(comboBox.getValue());
                    }
                });
            }
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    AttendanceRecord record = getTableRow().getItem();
                    if (record != null) {
                        comboBox.setValue(record.getGrade());
                    }
                    setGraphic(comboBox);
                }
            }
        });

        attendanceTable.getColumns().addAll(nameCol, subjectCol, presentCol, gradeCol);
        attendanceTable.setItems(attendanceRecords);
    }

    // Добавление записей посещаемости для студента
    private void addAttendanceRecord(Student student) {
        for (Subject subject : subjects) {
            AttendanceRecord record = new AttendanceRecord(student.getId(), student.getName(), subject);
            attendanceRecords.add(record);
        }
    }

    // Диалоговое окно для добавления студента
    private void showAddStudentDialog() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Добавить студента");


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        TextField groupField = new TextField();

        grid.add(new Label("ФИО:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Группа:"), 0, 1);
        grid.add(groupField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                int id = students.size() + 1;
                return new Student(id, nameField.getText(), groupField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(student -> {
            students.add(student);
            // Добавление записей посещаемости для нового студента
            addAttendanceRecord(student);
        });
    }

    // Диалоговое окно для добавления предмета
    private void showAddSubjectDialog() {
        Dialog<Subject> dialog = new Dialog<>();
        dialog.setTitle("Добавить предмет");


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();

        grid.add(new Label("Название предмета:"), 0, 0);
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                int id = subjects.size() + 1;
                return new Subject(id, nameField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(subject -> {
            subjects.add(subject);
        });
    }
}
