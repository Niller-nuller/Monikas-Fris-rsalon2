package org.example.monikasfrisrsalon2;

import javafx.application.Application;
import org.example.monikasfrisrsalon2.a_controller.HelloController;
import org.example.monikasfrisrsalon2.b_service.Service;
import org.example.monikasfrisrsalon2.c_model.Customer;
import org.example.monikasfrisrsalon2.d_dbconfig.DbConnect;
import org.example.monikasfrisrsalon2.e_repository.*;

import java.sql.SQLException;
import java.util.Timer;

public class Launcher {
    public static void main(String[] args) throws SQLException {
        DbConnect dbConnect = new DbConnect();
        BookingRepo bookingRepo = new BookingRepo();
        CustomerRepo customerRepo = new CustomerRepo();
        HairDresserRepo hairDresserRepo = new HairDresserRepo();
        TreatmentRepo treatmentRepo = new TreatmentRepo();
        OperatorRepo operatorRepo = new OperatorRepo();

        Timer timer = new Timer();

        Service service = new Service(bookingRepo,customerRepo,hairDresserRepo,treatmentRepo,operatorRepo);

        Application.launch(HelloApplication.class, args);
    }
}
