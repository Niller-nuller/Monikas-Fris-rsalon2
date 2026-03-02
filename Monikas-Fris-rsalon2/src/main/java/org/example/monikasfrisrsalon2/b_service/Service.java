package org.example.monikasfrisrsalon2.b_service;

import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import jdk.jfr.Timespan;
import org.example.monikasfrisrsalon2.c_model.*;
import org.example.monikasfrisrsalon2.e_repository.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.security.spec.ECField;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Service {
    private final BookingRepo bookingRepo;
    private final CustomerRepo customerRepo;
    private final HairDresserRepo hairDresserRepo;
    private final TreatmentRepo treatmentRepo;
    private final OperatorRepository operatorRepo;
    private final ServiceLogin serviceLogin;

    Operator currentOperator;
    
    public Service(BookingRepo bookingRepo, CustomerRepo customerRepo, HairDresserRepo hairDresserRepo, TreatmentRepo treatmentRepo, OperatorRepository operatorRepo, ServiceLogin serviceLogin) {
        this.bookingRepo = bookingRepo;
        this.customerRepo = customerRepo;
        this.hairDresserRepo = hairDresserRepo;
        this.treatmentRepo = treatmentRepo;
        this.operatorRepo = operatorRepo;
        this.serviceLogin = serviceLogin;
    }

    //--------------- Control----------------------------------------------------------------------------------

    public List<Booking> handleGetPendingBookings(LocalDate date) throws SQLException {
        return bookingRepo.getBookingListBasedOnStatus(Status.Pending, date);
    }
    public void cancelBooking(Booking booking) throws SQLException {
        bookingRepo.chancelBooking(booking);
    }

    // TEST

    public List<Booking> getActiveBookings() {
        System.out.println("GET ACTIVE BOOKINGS ACTIVATED");
        List<Booking> aBookings = new ArrayList<>();
        LocalDateTime lDate = LocalDateTime.of(2026, 02, 27, 14, 30);
        aBookings.add(new Booking(1, "Jonas", "23907290", "Hej@gmail.com",TreatmentType.MandCut,45,lDate, "Monika",Status.Pending));
        return aBookings;
    }
    
    public void getOperator(){
        this.currentOperator = serviceLogin.getOperator();
    }
}
