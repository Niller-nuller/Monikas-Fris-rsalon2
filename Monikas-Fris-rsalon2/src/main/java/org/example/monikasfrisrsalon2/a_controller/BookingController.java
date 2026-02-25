package org.example.monikasfrisrsalon2.a_controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import static java.awt.Color.white;


public class BookingController {

    public record AppointmentRow(String customer, String service, String employee, LocalDateTime start) {}

    @FXML private MFXPaginatedTableView<AppointmentRow> active_AppointmentsTable;
    @FXML private MFXPaginatedTableView<AppointmentRow> history_AppointmentsTable;

    private MFXTableColumn<AppointmentRow> timeCol;
    private MFXTableColumn<AppointmentRow> customerCol;
    private MFXTableColumn<AppointmentRow> serviceCol;
    private MFXTableColumn<AppointmentRow> employeeCol;
    private MFXTableColumn<AppointmentRow> inspectCol;
    private MFXTableColumn<AppointmentRow> deleteCol;

    public void initialize () {
        createActiveTableView();
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

        // ------------------------------- ICONS ------------------------------------ //
        FontIcon inspectIcon = new FontIcon("far-address-card");
        inspectIcon.setIconSize(13);
        inspectIcon.setIconColor(Color.WHITE);

        FontIcon deleteIcon = new FontIcon("far-window-close");
        inspectIcon.setIconSize(13);
        inspectIcon.setIconColor(Color.WHITE);
        // -------------------------------------------------------------------------- //

        inspectCol.setRowCellFactory(item -> {
            var cell = new MFXTableRowCell<AppointmentRow, String>(r -> "");
            var btn = new MFXButton();
            btn.setGraphic(inspectIcon);
            //btn.setOnAction(e -> inspectAppointment(appointment));
            cell.setTrailingGraphic(btn);
            return cell;
        });

        deleteCol.setRowCellFactory(item -> {
            var cell = new MFXTableRowCell<AppointmentRow, String>(r -> "");
            var btn = new MFXButton();
            btn.setGraphic(deleteIcon);
            //btn.setOnAction(e -> deleteAppointment(appointment));
            cell.setTrailingGraphic(btn);
            return cell;
        });

        timeCol.setRowCellFactory(r -> new MFXTableRowCell<>(a -> a.start().format(fmt)));
        customerCol.setRowCellFactory(r -> new MFXTableRowCell<>(AppointmentRow::customer));
        serviceCol.setRowCellFactory(r -> new MFXTableRowCell<>(AppointmentRow::service));
        employeeCol.setRowCellFactory(r -> new MFXTableRowCell<>(AppointmentRow::employee));

        active_AppointmentsTable.getTableColumns().setAll(timeCol, customerCol, serviceCol, employeeCol);

        active_AppointmentsTable.setItems(FXCollections.observableArrayList(
                /*
                new AppointmentRow(name,treatment, hairdresser, time);
                */
        ));

        active_AppointmentsTable.setRowsPerPage(18);


        Platform.runLater(this::adjustColumnWidths);


        active_AppointmentsTable.widthProperty().addListener((obs, oldW, newW) -> adjustColumnWidths());
    }

    ///  HISTORY OF BOOKING - TABLEVIEW
    private void createHistoryTableView () {
        var fmt = DateTimeFormatter.ofPattern("dd-MM HH:mm");

        timeCol = new MFXTableColumn<>("Tid", true, Comparator.comparing(AppointmentRow::start));
        customerCol = new MFXTableColumn<>("Kunde", true, Comparator.comparing(AppointmentRow::customer));
        serviceCol = new MFXTableColumn<>("Behandling", true, Comparator.comparing(AppointmentRow::service));
        employeeCol = new MFXTableColumn<>("Medarbejder", true, Comparator.comparing(AppointmentRow::employee));

        timeCol.setRowCellFactory(r -> new MFXTableRowCell<>(a -> a.start().format(fmt)));
        customerCol.setRowCellFactory(r -> new MFXTableRowCell<>(AppointmentRow::customer));
        serviceCol.setRowCellFactory(r -> new MFXTableRowCell<>(AppointmentRow::service));
        employeeCol.setRowCellFactory(r -> new MFXTableRowCell<>(AppointmentRow::employee));

        history_AppointmentsTable.getTableColumns().setAll(timeCol, customerCol, serviceCol, employeeCol);

        history_AppointmentsTable.setItems(FXCollections.observableArrayList(
                /*
                new AppointmentRow(name,treatment, hairdresser, time);
                */
        ));

        history_AppointmentsTable.setRowsPerPage(18);


        Platform.runLater(this::adjustColumnWidths);


        history_AppointmentsTable.widthProperty().addListener((obs, oldW, newW) -> adjustColumnWidths());
    }

    ///  ADJUST COLUMN WIDTH FROM TABLEVIEW WIDTH
    private void adjustColumnWidths() {
        double w = active_AppointmentsTable.getWidth();
        if (w <= 0) return;

        double usable = w - 30;
        setFixedWidth(timeCol, usable * 0.20);
        setFixedWidth(customerCol, usable * 0.30);
        setFixedWidth(serviceCol, usable * 0.30);
        setFixedWidth(employeeCol, usable * 0.20);
    }

    ///  APPLY CALCULATED WIDTH
    private static void setFixedWidth(MFXTableColumn<?> col, double w) {
        if (w <= 0) return;
        col.setMinWidth(w);
        col.setPrefWidth(w);
        col.setMaxWidth(w);
    }

}

