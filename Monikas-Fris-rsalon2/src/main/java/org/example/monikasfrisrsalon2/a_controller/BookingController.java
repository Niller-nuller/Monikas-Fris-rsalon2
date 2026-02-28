package org.example.monikasfrisrsalon2.a_controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.example.monikasfrisrsalon2.b_service.Service;
import org.example.monikasfrisrsalon2.c_model.Booking;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.function.Function;

import static java.awt.Color.white;


public class BookingController {

    private final Service service;

    public BookingController (Service service) {
        this.service = service;
    }

    private final ObservableList<AppointmentRow> activeRows = FXCollections.observableArrayList();
    private final ObservableList<AppointmentRow> historyRows = FXCollections.observableArrayList();

    @FXML private MFXPaginatedTableView<AppointmentRow> active_AppointmentsTable;
    @FXML private MFXPaginatedTableView<AppointmentRow> history_AppointmentsTable;

    private MFXTableColumn<AppointmentRow> timeCol;
    private MFXTableColumn<AppointmentRow> customerCol;
    private MFXTableColumn<AppointmentRow> serviceCol;
    private MFXTableColumn<AppointmentRow> employeeCol;
    private MFXTableColumn<AppointmentRow> inspectCol;
    private MFXTableColumn<AppointmentRow> deleteCol;

    private static final double ROW_H = 52;
    private static final double ACTION_BTN = 28;

    // NEW

    @FXML private AnchorPane mainPane;
    @FXML private StackPane overlay;
    @FXML private AnchorPane popup;

    @FXML private TextField nameField;
    @FXML private TextField emailField;


    //

    @FXML
    public void initialize () {
        createActiveTableView();
        active_AppointmentsTable.setRowsPerPage(5);

        // run after control is attached (safer with virtualized controls)
        active_AppointmentsTable.sceneProperty().addListener((obs, old, scene) -> {
            if (scene != null) Platform.runLater(this::reloadActiveBookings);
        });

        /*
        createHistoryTableView();
        reloadHistoryBookings();

         */

        // NEW
        overlay.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getTarget() == overlay) closePopup();
        });

        overlay.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ESCAPE -> closePopup();
            }
        });
        overlay.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    }

    public record AppointmentRow(
            String customer,
            String service,
            String employee,
            LocalDateTime start
    ) {

    }

    private AppointmentRow toRow(Booking b) {
        return new AppointmentRow(
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

    private MFXTableRowCell<AppointmentRow, String> centeredActionCell(String iconLiteral) {
        return new MFXTableRowCell<AppointmentRow, String>(r -> "") {

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
            }
        };
    }

    ///  ACTIVE BOOKINGS - TABLE VIEW
    private void createActiveTableView () {
        var fmt = DateTimeFormatter.ofPattern("dd-MM HH:mm");

        timeCol = new MFXTableColumn<>("Tid", true, Comparator.comparing(AppointmentRow::start));
        customerCol = new MFXTableColumn<>("Kunde", true, Comparator.comparing(AppointmentRow::customer));
        serviceCol = new MFXTableColumn<>("Behandling", true, Comparator.comparing(AppointmentRow::service));
        employeeCol = new MFXTableColumn<>("Medarbejder", true, Comparator.comparing(AppointmentRow::employee));
        inspectCol = new MFXTableColumn<>("Inspect", false, null);
        deleteCol  = new MFXTableColumn<>("Delete",  false, null);

        inspectCol.setRowCellFactory(row -> centeredActionCell("far-address-card"));
        deleteCol.setRowCellFactory(row -> centeredActionCell("far-window-close"));

        inspectCol.setMinWidth(60); inspectCol.setPrefWidth(60); inspectCol.setMaxWidth(60);
        deleteCol.setMinWidth(60);  deleteCol.setPrefWidth(60);  deleteCol.setMaxWidth(60);

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

    private void createHistoryTableView () {
        var fmt = DateTimeFormatter.ofPattern("dd-MM HH:mm");

        timeCol = new MFXTableColumn<>("Tid", true, Comparator.comparing(AppointmentRow::start));
        customerCol = new MFXTableColumn<>("Kunde", true, Comparator.comparing(AppointmentRow::customer));
        serviceCol = new MFXTableColumn<>("Behandling", true, Comparator.comparing(AppointmentRow::service));
        employeeCol = new MFXTableColumn<>("Medarbejder", true, Comparator.comparing(AppointmentRow::employee));
        inspectCol = new MFXTableColumn<>("Inspect", false, null);

        inspectCol.setRowCellFactory(row -> centeredActionCell("far-address-card"));

        inspectCol.setMinWidth(60); inspectCol.setPrefWidth(60); inspectCol.setMaxWidth(60);

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

    private void reloadActiveBookings() {
        var rows = service.getActiveBookings()
                .stream()
                .map(this::toRow)
                .toList();

        // IMPORTANT: new list instance each time
        active_AppointmentsTable.setItems(FXCollections.observableArrayList(rows));

        active_AppointmentsTable.currentPageProperty().set(0);

        System.out.println("rows=" + rows.size()
                + " tableItems=" + active_AppointmentsTable.getItems().size());
    }
    /*
    private void reloadHistoryBookings() {
        historyData.setAll(service.getHistoryBookings());
    }

    @FXML
    private void onSearch() {
        activeData.setAll(service.getBookingWithName(searchField.getText()));
    }
     */


    // NEW

    @FXML
    private void openPopup() {
        overlay.setManaged(true);
        overlay.setVisible(true);

        mainPane.setDisable(true);

        nameField.requestFocus();
    }

    @FXML
    private void closePopup() {
        overlay.setVisible(false);
        overlay.setManaged(false);

        mainPane.setDisable(false);
    }

    @FXML
    private void savePopup() {

        // TODO: POPUP_LOGIC: BOOKING

        closePopup();
    }
}