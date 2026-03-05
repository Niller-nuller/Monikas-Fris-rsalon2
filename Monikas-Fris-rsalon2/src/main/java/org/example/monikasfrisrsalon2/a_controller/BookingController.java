package org.example.monikasfrisrsalon2.a_controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.example.monikasfrisrsalon2.b_service.Service;
import org.example.monikasfrisrsalon2.b_service.ServiceLogin;
import org.example.monikasfrisrsalon2.c_model.Booking;
import org.example.monikasfrisrsalon2.c_model.Operator;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;


public class BookingController {

    private final Service service;
    private final ServiceLogin serviceLogin;

    public BookingController(Service service, ServiceLogin serviceLogin) {
        this.service = service;
        this.serviceLogin = serviceLogin;
    }


    @FXML
    private MFXPaginatedTableView<AppointmentRow> active_AppointmentsTable;
    @FXML
    private MFXPaginatedTableView<AppointmentRow> history_AppointmentsTable;

    private org.example.monikasfrisrsalon2.c_model.Treatment selectedTreatment;
    private org.example.monikasfrisrsalon2.c_model.Hairdresser selectedHairdresser;
    private LocalDateTime selectedStartTime;
    @FXML private Label operatorName;

    @FXML private DatePicker viewingDatePicker;

    private LocalDate selectedDate;

    private MFXTableColumn<AppointmentRow> timeCol;
    private MFXTableColumn<AppointmentRow> customerCol;
    private MFXTableColumn<AppointmentRow> serviceCol;
    private MFXTableColumn<AppointmentRow> employeeCol;
    private MFXTableColumn<AppointmentRow> inspectCol;
    private MFXTableColumn<AppointmentRow> deleteCol;

    private static final double ROW_H = 52;
    private static final double ACTION_BTN = 28;

    @FXML
    private AnchorPane mainPane;

    // Create Customer Tab
    @FXML
    private StackPane overlayForCreateCustomer;
    @FXML
    private AnchorPane createCustomerTab;

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    // Inspect Customer Tab
    @FXML
    private StackPane overlayInspectCustomerTab;
    @FXML
    private AnchorPane inspectCustomerTab;


    ///  SECTION FOR CREATE BOOKING
    @FXML private TextField phoneField;

    @FXML private DatePicker bookingDatePicker;

    @FXML private VBox treatmentSection;
    @FXML private VBox hairdresserSection;
    @FXML private VBox slotSection;

    @FXML private FlowPane treatmentPane;
    @FXML private FlowPane hairdresserPane;
    @FXML private FlowPane slotPane;

    @FXML private Label createBookingErrorLabel;
    @FXML private Button createBookingBtn;

    ///  SECTION FOR INSPECT DATA

    @FXML private Label customerName;
    @FXML private Label customerEmail;
    @FXML private Label customerTlfNumber;
    @FXML private Label customerDate;
    @FXML private Label customerHairdresserName;
    @FXML private Label customerTreatmentName;
    @FXML private Label customerStatus;


    //

    @FXML
    public void initialize() {

        operatorName.setText(serviceLogin.getOperator().getUsername());


        selectedDate = LocalDate.now();

        viewingDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate == null) return;
            selectedDate = newDate;
            reloadActiveBookings();
            reloadHistoryBookings();
        });


        viewingDatePicker.setValue(selectedDate);

        attachStylesheetWhenReady(mainPane, "/org/example/monikasfrisrsalon2/Styles/Style.css");

        createActiveTableView();
        active_AppointmentsTable.setRowsPerPage(5);

        createHistoryTableView();
        history_AppointmentsTable.setRowsPerPage(5);


        Runnable initialLoad = () -> {

            selectedDate = viewingDatePicker.getValue();
            reloadActiveBookings();
            reloadHistoryBookings();
        };

        mainPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) Platform.runLater(initialLoad);
        });

        if (mainPane.getScene() != null) {
            Platform.runLater(initialLoad);
        }

        // NEW
        overlayForCreateCustomer.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getTarget() == overlayForCreateCustomer) closeCreateCustomerTab();
        });

        overlayForCreateCustomer.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ESCAPE -> closeCreateCustomerTab();
            }
        });
        overlayForCreateCustomer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        overlayInspectCustomerTab.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getTarget() == overlayInspectCustomerTab) closeInspectCustomerTab();
        });

        overlayInspectCustomerTab.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ESCAPE -> closeInspectCustomerTab();
            }
        });
        overlayInspectCustomerTab.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        setupBookingWizard();
    }

    public record AppointmentRow(
            Booking booking,
            String customer,
            String service,
            String employee,
            LocalDateTime start
    ) {

    }

    private AppointmentRow toRow(Booking b) {
        return new AppointmentRow(
                b,
                b.getName(),
                String.valueOf(b.getTreatment()),
                b.getHairdresser(),
                b.getDateTime()
        );
    }

    private static <T> MFXTableRowCell<T, String> fixedTextCell(java.util.function.Function<T, String> extractor) {
        var cell = new MFXTableRowCell<T, String>(extractor);
        cell.getStyleClass().add("appt-text-cell");
        cell.setAlignment(Pos.CENTER_LEFT);

        cell.setMinHeight(ROW_H);
        cell.setPrefHeight(ROW_H);
        cell.setMaxHeight(ROW_H);

        return cell;
    }

    private MFXTableRowCell<AppointmentRow, String> centeredActionCell(String iconLiteral, java.util.function.Consumer<AppointmentRow> action) {
        return new MFXTableRowCell<>(r -> "") {

            private final MFXButton btn = new MFXButton();
            private final StackPane box = new StackPane(btn);

            {
                setMinHeight(ROW_H);
                setPrefHeight(ROW_H);
                setMaxHeight(ROW_H);
                setAlignment(Pos.CENTER);

                setText(null);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(box);

                box.setMinHeight(ROW_H);
                box.setPrefHeight(ROW_H);
                box.setMaxHeight(ROW_H);
                box.setAlignment(Pos.CENTER);

                btn.setText(null);
                btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                btn.setFocusTraversable(false);
                btn.getStyleClass().add("table-action-btn");

                btn.setMinSize(ACTION_BTN, ACTION_BTN);
                btn.setPrefSize(ACTION_BTN, ACTION_BTN);
                btn.setMaxSize(ACTION_BTN, ACTION_BTN);

                var icon = new FontIcon(iconLiteral);
                icon.setIconSize(14);
                icon.setIconColor(Color.BLACK);
                btn.setGraphic(icon);
            }

            @Override
            public void update(AppointmentRow item) {
                super.update(item);
                btn.setDisable(item == null);

                btn.setOnAction(e -> {
                    if (item != null) action.accept(item);
                    e.consume();
                });
            }
        };
    }

    ///  ACTIVE BOOKINGS - TABLE VIEW
    private void createActiveTableView() {
        var fmt = DateTimeFormatter.ofPattern("dd-MM HH:mm");

        timeCol = new MFXTableColumn<>("Tid", true, Comparator.comparing(AppointmentRow::start));
        customerCol = new MFXTableColumn<>("Kunde", true, Comparator.comparing(AppointmentRow::customer));
        serviceCol = new MFXTableColumn<>("Behandling", true, Comparator.comparing(AppointmentRow::service));
        employeeCol = new MFXTableColumn<>("Medarbejder", true, Comparator.comparing(AppointmentRow::employee));
        inspectCol = new MFXTableColumn<>("Inspect", false, null);
        deleteCol = new MFXTableColumn<>("Delete", false, null);

        inspectCol.setRowCellFactory(row -> centeredActionCell("far-address-card", this::onInspectRow));
        deleteCol.setRowCellFactory(row -> centeredActionCell("far-window-close", this::onDeleteRow));

        inspectCol.setMinWidth(60);
        inspectCol.setPrefWidth(60);
        inspectCol.setMaxWidth(60);
        deleteCol.setMinWidth(60);
        deleteCol.setPrefWidth(60);
        deleteCol.setMaxWidth(60);

        active_AppointmentsTable.autosizeColumnsOnInitialization();
        active_AppointmentsTable.setTableRowFactory(item -> sizedRow(active_AppointmentsTable, item));

        timeCol.setRowCellFactory(r -> fixedTextCell(a -> a.start() == null ? "" : a.start().format(fmt)));
        customerCol.setRowCellFactory(r -> fixedTextCell(AppointmentRow::customer));
        serviceCol.setRowCellFactory(r -> fixedTextCell(AppointmentRow::service));
        employeeCol.setRowCellFactory(r -> fixedTextCell(AppointmentRow::employee));

        timeCol.setPrefWidth(150);
        customerCol.setPrefWidth(225);
        serviceCol.setPrefWidth(225);
        employeeCol.setPrefWidth(300);

        active_AppointmentsTable.getTableColumns().setAll(timeCol, customerCol, serviceCol, employeeCol, inspectCol, deleteCol);

        active_AppointmentsTable.setRowsPerPage(5);

        System.out.println("CREATE ACTIVE TABLE RAN");
    }

    private void createHistoryTableView() {
        var fmt = DateTimeFormatter.ofPattern("dd-MM HH:mm");

        timeCol = new MFXTableColumn<>("Tid", true, Comparator.comparing(AppointmentRow::start));
        customerCol = new MFXTableColumn<>("Kunde", true, Comparator.comparing(AppointmentRow::customer));
        serviceCol = new MFXTableColumn<>("Behandling", true, Comparator.comparing(AppointmentRow::service));
        employeeCol = new MFXTableColumn<>("Medarbejder", true, Comparator.comparing(AppointmentRow::employee));
        inspectCol = new MFXTableColumn<>("Inspect", false, null);

        inspectCol.setRowCellFactory(row -> centeredActionCell("far-address-card", this::onInspectRow));

        inspectCol.setMinWidth(60);
        inspectCol.setPrefWidth(60);
        inspectCol.setMaxWidth(60);

        history_AppointmentsTable.autosizeColumnsOnInitialization();
        history_AppointmentsTable.setTableRowFactory(item -> sizedRow(history_AppointmentsTable, item));

        timeCol.setRowCellFactory(r -> fixedTextCell(a -> a.start() == null ? "" : a.start().format(fmt)));
        customerCol.setRowCellFactory(r -> fixedTextCell(AppointmentRow::customer));
        serviceCol.setRowCellFactory(r -> fixedTextCell(AppointmentRow::service));
        employeeCol.setRowCellFactory(r -> fixedTextCell(AppointmentRow::employee));

        timeCol.setPrefWidth(150);
        customerCol.setPrefWidth(225);
        serviceCol.setPrefWidth(225);
        employeeCol.setPrefWidth(300);

        history_AppointmentsTable.getTableColumns().setAll(timeCol, customerCol, serviceCol, employeeCol, inspectCol);

        history_AppointmentsTable.setRowsPerPage(5);
    }

    private MFXTableRow<AppointmentRow> sizedRow(MFXPaginatedTableView<AppointmentRow> table, AppointmentRow item) {
        var row = new MFXTableRow<>(table, item);
        row.setMinHeight(ROW_H);
        row.setPrefHeight(ROW_H);
        row.setMaxHeight(ROW_H);
        return row;
    }

    private void reloadActiveBookings(){
        try {
            var rows = service.getAllPendingBookings(selectedDate)
                    .stream()
                    .map(this::toRow)
                    .toList();

        active_AppointmentsTable.setItems(FXCollections.observableArrayList(rows));

        active_AppointmentsTable.currentPageProperty().set(0);

        System.out.println("rows=" + rows.size()
                + " tableItems=" + active_AppointmentsTable.getItems().size());
            System.out.println(selectedDate);
        } catch (RuntimeException e) {
            System.out.println("Error loading active bookings");
            System.out.println(selectedDate);
        }
    }

    private void reloadHistoryBookings(){
        try {
            var rows = service.getAllBookings()
                    .stream()
                    .map(this::toRow)
                    .toList();

            history_AppointmentsTable.setItems(FXCollections.observableArrayList(rows));

            history_AppointmentsTable.currentPageProperty().set(0);

            System.out.println("rows=" + rows.size()
                    + " tableItems=" + history_AppointmentsTable.getItems().size());
        } catch (RuntimeException e) {
            System.out.println("Error loading active bookings");
        }
    }

    // NEW

    @FXML
    private void openCreateCustomerTab() {
        overlayForCreateCustomer.setManaged(true);
        overlayForCreateCustomer.setVisible(true);

        mainPane.setDisable(true);


        nameField.clear();
        emailField.clear();
        phoneField.clear();

        selectedTreatment = null;
        selectedHairdresser = null;
        selectedStartTime = null;

        createBookingErrorLabel.setText("");
        createBookingBtn.setDisable(false);


        bookingDatePicker.setValue(java.time.LocalDate.now());


        treatmentPane.getChildren().clear();
        hairdresserPane.getChildren().clear();
        slotPane.getChildren().clear();

        hideSection(hairdresserSection);
        hideSection(slotSection);


        loadAndShowTreatments();

        nameField.requestFocus();
    }

    @FXML
    private void closeCreateCustomerTab() {
        overlayForCreateCustomer.setVisible(false);
        overlayForCreateCustomer.setManaged(false);

        mainPane.setDisable(false);
    }

    @FXML
    private void inspectCustomerTab() {
        overlayInspectCustomerTab.setManaged(true);
        overlayInspectCustomerTab.setVisible(true);
        mainPane.setDisable(true);
        overlayInspectCustomerTab.requestFocus();
    }

    @FXML
    private void closeInspectCustomerTab() {
        overlayInspectCustomerTab.setVisible(false);
        overlayInspectCustomerTab.setManaged(false);
        mainPane.setDisable(false);
    }

    @FXML
    private void setInspectData(String name,
                                String email,
                                String tlf_number,
                                LocalDateTime date,
                                String hairdresserName,
                                String treatmentName,
                                String status) {
        customerName.setText(name);
        customerEmail.setText(email);
        customerTlfNumber.setText(tlf_number);
        var fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        customerDate.setText(date == null ? "" : date.format(fmt));
        customerHairdresserName.setText(hairdresserName);
        customerTreatmentName.setText(treatmentName);
        customerStatus.setText(status);
    }

    private void onInspectRow(AppointmentRow row) {
        Booking b = row.booking();
        setInspectData(b.getName(),b.getEmail(),b.getPhoneNumber(),b.getDateTime(),b.getHairdresser(),String.valueOf(b.getTreatment()), String.valueOf(b.getStatus()));
        inspectCustomerTab();
    }

    private void onDeleteRow(AppointmentRow row) {
        Booking b = row.booking();
        try {
            service.cancelBooking(b);


            reloadActiveBookings();
            reloadHistoryBookings();
        } catch (SQLException e) {
            System.out.println("Delete Fejl: " + e.getMessage());
            e.printStackTrace();
        }
    }

    ///  NEW - CREATE BOOKING

    @FXML
    private void onCreateBooking(ActionEvent event) {
        createBookingErrorLabel.setText("");

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            createBookingErrorLabel.setText("Udfyld venligst navn, email og telefon.");
            return;
        }
        if (selectedTreatment == null || selectedHairdresser == null || selectedStartTime == null) {
            createBookingErrorLabel.setText("Vælg behandling, medarbejder og tid.");
            return;
        }

        createBookingBtn.setDisable(true);

        Task<Void> task = getVoidTask(name, email, phone);

        new Thread(task).start();
    }

    private Task<Void> getVoidTask(String name, String email, String phone) {
        Task<Void> task = new Task<>() {
            @Override protected Void call() throws Exception {
                service.createBooking(name, email, phone, selectedTreatment, selectedHairdresser, selectedStartTime);
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            createBookingBtn.setDisable(false);
            closeCreateCustomerTab();
            reloadActiveBookings();
        });

        task.setOnFailed(e -> {
            createBookingBtn.setDisable(false);
            createBookingErrorLabel.setText("Kunne ikke oprette booking: " + task.getException().getMessage());

        });
        return task;
    }

    private void setupBookingWizard() {
        bookingDatePicker.setValue(java.time.LocalDate.now());

        bookingDatePicker.valueProperty().addListener((obs, oldV, newV) -> {

            selectedStartTime = null;
            hideSection(slotSection);
            slotPane.getChildren().clear();


            if (selectedTreatment != null && selectedHairdresser != null) {
                loadAndShowSlots();
            }
        });


        loadAndShowTreatments();
    }

    private void loadAndShowTreatments() {
        treatmentPane.getChildren().clear();
        hideSection(hairdresserSection);
        hideSection(slotSection);

        selectedTreatment = null;
        selectedHairdresser = null;
        selectedStartTime = null;

        javafx.concurrent.Task<java.util.List<org.example.monikasfrisrsalon2.c_model.Treatment>> task =
                new javafx.concurrent.Task<>() {
                    @Override protected java.util.List<org.example.monikasfrisrsalon2.c_model.Treatment> call() throws Exception {
                        return service.getTreatments();
                    }
                };

        task.setOnSucceeded(e -> {
            var treatments = task.getValue();
            populateSelectionButtons(
                    treatmentPane,
                    treatments,
                    org.example.monikasfrisrsalon2.c_model.Treatment::getName,
                    t -> {
                        selectedTreatment = t;


                        selectedHairdresser = null;
                        selectedStartTime = null;
                        slotPane.getChildren().clear();
                        hideSection(slotSection);

                        loadAndShowHairdressers(t);
                    }
            );
        });

        task.setOnFailed(e -> createBookingErrorLabel.setText("Kunne ikke hente behandlinger: " + task.getException().getMessage()));

        new Thread(task).start();
    }

    private void loadAndShowHairdressers(org.example.monikasfrisrsalon2.c_model.Treatment treatment) {
        hairdresserPane.getChildren().clear();
        showSection(hairdresserSection);

        javafx.concurrent.Task<java.util.List<org.example.monikasfrisrsalon2.c_model.Hairdresser>> task =
                new javafx.concurrent.Task<>() {
                    @Override protected java.util.List<org.example.monikasfrisrsalon2.c_model.Hairdresser> call() throws Exception {
                        return service.getEligibleHairdressers(treatment);
                    }
                };

        task.setOnSucceeded(e -> {
            var hairdressers = task.getValue();
            populateSelectionButtons(
                    hairdresserPane,
                    hairdressers,
                    org.example.monikasfrisrsalon2.c_model.Hairdresser::getName,
                    h -> {
                        selectedHairdresser = h;

                        // Click opens next section:
                        selectedStartTime = null;
                        loadAndShowSlots();
                    }
            );
        });

        task.setOnFailed(e -> createBookingErrorLabel.setText("Kunne ikke hente medarbejdere: " + task.getException().getMessage()));

        new Thread(task).start();
    }

    private void loadAndShowSlots() {
        slotPane.getChildren().clear();
        showSection(slotSection);

        var date = bookingDatePicker.getValue();
        if (date == null || selectedTreatment == null || selectedHairdresser == null) return;

        javafx.concurrent.Task<java.util.List<Service.TimeSlot>> task =
                new javafx.concurrent.Task<>() {
                    @Override protected java.util.List<Service.TimeSlot> call() throws Exception {
                        return service.getTimeSlots(date, selectedHairdresser, selectedTreatment);
                    }
                };

        task.setOnSucceeded(e -> {
            var slots = task.getValue();
            populateSlotButtons(slots);
        });

        task.setOnFailed(e -> createBookingErrorLabel.setText("Kunne ikke hente tider: " + task.getException().getMessage()));

        new Thread(task).start();
    }

    private <T> void populateSelectionButtons(
            FlowPane pane,
            java.util.List<T> items,
            java.util.function.Function<T, String> labelFn,
            java.util.function.Consumer<T> onSelect
    ) {
        pane.getChildren().clear();

        final java.util.concurrent.atomic.AtomicReference<javafx.scene.control.ButtonBase> selectedBtnRef =
                new java.util.concurrent.atomic.AtomicReference<>(null);

        for (T item : items) {

            MFXButton btn = new MFXButton(labelFn.apply(item));
            btn.getStyleClass().addAll("wizard");

            btn.setOnAction(ev -> {
                // visual selection
                var prev = selectedBtnRef.get();
                if (prev != null) prev.getStyleClass().remove("selected");

                btn.getStyleClass().add("selected");
                selectedBtnRef.set(btn);

                onSelect.accept(item);
            });

            pane.getChildren().add(btn);
        }
    }

    private void populateSlotButtons(java.util.List<Service.TimeSlot> slots) {
        slotPane.getChildren().clear();

        final java.util.concurrent.atomic.AtomicReference<javafx.scene.control.ButtonBase> selectedBtnRef =
                new java.util.concurrent.atomic.AtomicReference<>(null);

        var fmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

        for (var slot : slots) {
            MFXButton btn = new MFXButton(slot.start().format(fmt));
            btn.getStyleClass().addAll("wizard");

            if (!slot.available()) {
                btn.setDisable(true);
            } else {
                btn.setOnAction(e -> {
                    var prev = selectedBtnRef.get();
                    if (prev != null) prev.getStyleClass().remove("selected");

                    btn.getStyleClass().add("selected");
                    selectedBtnRef.set(btn);

                    selectedStartTime = slot.start();
                });
            }

            slotPane.getChildren().add(btn);
        }
    }

    private void showSection(VBox box) {
        box.setManaged(true);
        box.setVisible(true);
    }

    private void hideSection(VBox box) {
        box.setVisible(false);
        box.setManaged(false);
    }

    private void attachStylesheetWhenReady(Region node, String preferredPath) {
        node.sceneProperty().addListener((obs, oldScene, scene) -> {
            if (scene == null) return;

            var url = getClass().getResource(preferredPath);


            if (url == null && preferredPath.startsWith("/org/example/")) {
                String dotted = preferredPath.replace("/org/example/monikasfrisrsalon2/", "/org.example.monikasfrisrsalon2/");
                url = getClass().getResource(dotted);
            }


            if (url == null) return;

            String cssUrl = url.toExternalForm();
            if (!scene.getStylesheets().contains(cssUrl)) {
                scene.getStylesheets().add(cssUrl);
            }
        });
    }

    @FXML
    private void logOut (ActionEvent event) {
        try{
            SceneNavigator.switchTo(event, "/org/example/monikasfrisrsalon2/Login.fxml");} catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}