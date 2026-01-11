package com.example.birthday;

import com.example.birthday.dao.BirthdayDao;
import com.example.birthday.model.Classmate;
import com.example.birthday.util.BirthdayUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class MainApp extends Application {

    private final BirthdayDao dao = new BirthdayDao();
    private final ObservableList<Classmate> data = FXCollections.observableArrayList();

    // ফর্ম ইনপুট
    private final TextField নামField = new TextField();
    private final DatePicker জন্মতারিখPicker = new DatePicker();
    private final TextField ফোনField = new TextField();
    private final TextField নোটField = new TextField();

    // সার্চ
    private final TextField নামসার্চField = new TextField();
    private final ComboBox<Integer> মাসCombo = new ComboBox<>();

    private TableView<Classmate> table;

    @Override
    public void start(Stage stage) {
        stage.setTitle("ক্লাসমেট জন্মদিন ম্যানেজার");

        table = buildTable();

        // ফর্ম UI
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        form.add(new Label("নাম"), 0, 0);
        form.add(নামField, 1, 0);

        form.add(new Label("জন্মতারিখ"), 0, 1);
        form.add(জন্মতারিখPicker, 1, 1);

        form.add(new Label("ফোন"), 0, 2);
        form.add(ফোনField, 1, 2);

        form.add(new Label("নোট"), 0, 3);
        form.add(নোটField, 1, 3);

        নামField.setPromptText("নাম লিখুন");
        ফোনField.setPromptText("ফোন নম্বর (ঐচ্ছিক)");
        নোটField.setPromptText("মন্তব্য (ঐচ্ছিক)");
        জন্মতারিখPicker.setPromptText("জন্মতারিখ নির্বাচন করুন");

        Button যোগকরুনBtn = new Button("যোগ করুন");
        Button আপডেটBtn = new Button("আপডেট");
        Button ডিলিটBtn = new Button("ডিলিট");
        Button রিফ্রেশBtn = new Button("রিফ্রেশ");

        HBox actions = new HBox(10, যোগকরুনBtn, আপডেটBtn, ডিলিটBtn, রিফ্রেশBtn);
        actions.setAlignment(Pos.CENTER_LEFT);
        form.add(actions, 1, 4);

        // সার্চ/ফিল্টার UI
        মাসCombo.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12);
        মাসCombo.setPromptText("মাস নির্বাচন করুন (১-১২)");
        নামসার্চField.setPromptText("নাম দিয়ে খুঁজুন");

        Button নামসার্চBtn = new Button("নাম সার্চ");
        Button মাসসার্চBtn = new Button("মাস সার্চ");
        Button আসন্নBtn = new Button("আসন্ন জন্মদিন");
        Button সবBtn = new Button("সব দেখুন");

        HBox searchBox = new HBox(10,
                new Label("সার্চ:"), নামসার্চField, নামসার্চBtn,
                মাসCombo, মাসসার্চBtn,
                আসন্নBtn, সবBtn
        );
        searchBox.setPadding(new Insets(10));
        searchBox.setAlignment(Pos.CENTER_LEFT);

        Label status = new Label("স্ট্যাটাস: প্রস্তুত");
        status.setPadding(new Insets(8));

        BorderPane root = new BorderPane();
        root.setTop(new VBox(searchBox, new Separator()));
        root.setCenter(table);
        root.setRight(new VBox(new Label("তথ্য যোগ/আপডেট"), form));
        root.setBottom(status);

        BorderPane.setMargin(table, new Insets(10));
        VBox.setVgrow(form, Priority.ALWAYS);

        // Events
        যোগকরুনBtn.setOnAction(e -> {
            try {
                String নাম = নামField.getText().trim();
                LocalDate জন্মতারিখ = জন্মতারিখPicker.getValue();
                if (নাম.isEmpty() || জন্মতারিখ == null) {
                    alert("ত্রুটি", "অনুগ্রহ করে নাম এবং জন্মতারিখ দিন।");
                    return;
                }
                Classmate c = new Classmate(নাম, জন্মতারিখ, ফোনField.getText().trim(), নোটField.getText().trim());
                int newId = dao.add(c);
                status.setText("স্ট্যাটাস: যোগ হয়েছে (ID=" + newId + ")");
                clearForm();
                loadAll();
                checkTodayBirthdaysPopup(); // যোগ করার পরও চেক
            } catch (Exception ex) {
                alert("ডাটাবেজ ত্রুটি", ex.getMessage());
            }
        });

        আপডেটBtn.setOnAction(e -> {
            Classmate sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                alert("সতর্কতা", "আপডেট করার জন্য টেবিল থেকে একজন নির্বাচন করুন।");
                return;
            }
            try {
                String নাম = নামField.getText().trim();
                LocalDate জন্মতারিখ = জন্মতারিখPicker.getValue();
                if (নাম.isEmpty() || জন্মতারিখ == null) {
                    alert("ত্রুটি", "অনুগ্রহ করে নাম এবং জন্মতারিখ দিন।");
                    return;
                }
                sel.setName(নাম);
                sel.setDob(জন্মতারিখ);
                sel.setPhone(ফোনField.getText().trim());
                sel.setNotes(নোটField.getText().trim());
                dao.update(sel);
                status.setText("স্ট্যাটাস: আপডেট হয়েছে (ID=" + sel.getId() + ")");
                clearForm();
                loadAll();
            } catch (Exception ex) {
                alert("ডাটাবেজ ত্রুটি", ex.getMessage());
            }
        });

        ডিলিটBtn.setOnAction(e -> {
            Classmate sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                alert("সতর্কতা", "ডিলিট করার জন্য টেবিল থেকে একজন নির্বাচন করুন।");
                return;
            }
            if (!confirm("নিশ্চিত?", "আপনি কি ডিলিট করতে চান: " + sel.getName() + " ?")) return;

            try {
                dao.deleteById(sel.getId());
                status.setText("স্ট্যাটাস: ডিলিট হয়েছে (ID=" + sel.getId() + ")");
                clearForm();
                loadAll();
            } catch (Exception ex) {
                alert("ডাটাবেজ ত্রুটি", ex.getMessage());
            }
        });

        রিফ্রেশBtn.setOnAction(e -> loadAll());

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) fillForm(newV);
        });

        নামসার্চBtn.setOnAction(e -> {
            String k = নামসার্চField.getText().trim();
            if (k.isEmpty()) {
                alert("সতর্কতা", "নাম লিখে সার্চ করুন।");
                return;
            }
            try {
                data.setAll(dao.searchByName(k));
                status.setText("স্ট্যাটাস: নাম সার্চ ফলাফল = " + data.size());
            } catch (Exception ex) {
                alert("ডাটাবেজ ত্রুটি", ex.getMessage());
            }
        });

        মাসসার্চBtn.setOnAction(e -> {
            Integer m = মাসCombo.getValue();
            if (m == null) {
                alert("সতর্কতা", "মাস নির্বাচন করুন।");
                return;
            }
            try {
                data.setAll(dao.searchByMonth(m));
                status.setText("স্ট্যাটাস: মাস সার্চ ফলাফল = " + data.size());
            } catch (Exception ex) {
                alert("ডাটাবেজ ত্রুটি", ex.getMessage());
            }
        });

        আসন্নBtn.setOnAction(e -> showUpcoming());
        সবBtn.setOnAction(e -> loadAll());

        // Initial load
        loadAll();

        // আজকের জন্মদিন নোটিফিকেশন (অ্যাপ চালু হলে একবার)
        checkTodayBirthdaysPopup();

        // প্রতি 1 ঘন্টা পরপর চেক (ডেমো)
        Timeline tl = new Timeline(new KeyFrame(Duration.hours(1), ev -> checkTodayBirthdaysPopup()));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();

        stage.setScene(new Scene(root, 1100, 600));
        stage.show();
    }

    private TableView<Classmate> buildTable() {
        TableView<Classmate> tv = new TableView<>(data);

        TableColumn<Classmate, Integer> c1 = new TableColumn<>("আইডি");
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        c1.setPrefWidth(60);

        TableColumn<Classmate, String> c2 = new TableColumn<>("নাম");
        c2.setCellValueFactory(new PropertyValueFactory<>("name"));
        c2.setPrefWidth(200);

        TableColumn<Classmate, LocalDate> c3 = new TableColumn<>("জন্মতারিখ");
        c3.setCellValueFactory(new PropertyValueFactory<>("dob"));
        c3.setPrefWidth(120);

        TableColumn<Classmate, String> c4 = new TableColumn<>("ফোন");
        c4.setCellValueFactory(new PropertyValueFactory<>("phone"));
        c4.setPrefWidth(140);

        TableColumn<Classmate, String> c5 = new TableColumn<>("নোট");
        c5.setCellValueFactory(new PropertyValueFactory<>("notes"));
        c5.setPrefWidth(200);

        tv.getColumns().addAll(c1, c2, c3, c4, c5);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        return tv;
    }

    private void loadAll() {
        try {
            data.setAll(dao.getAll());
        } catch (Exception ex) {
            alert("ডাটাবেজ ত্রুটি", ex.getMessage());
        }
    }

    private void showUpcoming() {
        try {
            List<Classmate> all = dao.getAll();
            LocalDate today = LocalDate.now();
            all.sort(Comparator.comparing(c -> BirthdayUtil.nextOccurrence(c.getDob(), today)));
            data.setAll(all);
        } catch (Exception ex) {
            alert("ডাটাবেজ ত্রুটি", ex.getMessage());
        }
    }

    private void checkTodayBirthdaysPopup() {
        try {
            LocalDate today = LocalDate.now();
            List<Classmate> todayList = dao.birthdaysToday(today);
            if (!todayList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("আজ জন্মদিন:\n\n");
                for (Classmate c : todayList) {
                    sb.append("• ").append(c.getName()).append("\n");
                }
                alert("🎉 জন্মদিনের নোটিফিকেশন", sb.toString());
            }
        } catch (Exception ignored) {
            // চাইলে এখানে error alert দেখাতে পারেন
        }
    }

    private void fillForm(Classmate c) {
        নামField.setText(c.getName());
        জন্মতারিখPicker.setValue(c.getDob());
        ফোনField.setText(c.getPhone() == null ? "" : c.getPhone());
        নোটField.setText(c.getNotes() == null ? "" : c.getNotes());
    }

    private void clearForm() {
        নামField.clear();
        জন্মতারিখPicker.setValue(null);
        ফোনField.clear();
        নোটField.clear();
        table.getSelectionModel().clearSelection();
    }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private boolean confirm(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        return a.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
