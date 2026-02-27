package org.example.monikasfrisrsalon2.b_service;

import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import jdk.jfr.Timespan;
import org.example.monikasfrisrsalon2.c_model.*;
import org.example.monikasfrisrsalon2.e_repository.*;

import java.io.IOException;
import java.security.spec.ECField;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class Service {
    private final BookingRepo bookingRepo;
    private final CustomerRepo customerRepo;
    private final HairDresserRepo hairDresserRepo;
    private final TreatmentRepo treatmentRepo;
    private OperatorRepo operatorRepo;

    private Operator operator;

    public Service(BookingRepo bookingRepo, CustomerRepo customerRepo, HairDresserRepo hairDresserRepo, TreatmentRepo treatmentRepo, OperatorRepo operatorRepo) {
        this.bookingRepo = bookingRepo;
        this.customerRepo = customerRepo;
        this.hairDresserRepo = hairDresserRepo;
        this.treatmentRepo = treatmentRepo;
        this.operatorRepo = operatorRepo;

    }

    //--------------- Control----------------------------------------------------------------------------------
    public Boolean serviceLogin(String username, String password){
        validateOperator(username, password);
        Operator operator = createOperator(username,password);
//        operator = operatorRepo.authenticateOperator(operator);
        setOperatorlogin(operator);
        return false;
    }
    public void setOperatorlogin(Operator operator){
        this.operator = operator;
    }
    public void setOperatorlogout(){
       // operator
    }
    private Operator createOperator(String username, String password){
        return new Operator(username, password);
    }
    private void validateOperator(String username, String password){
        if(username.isBlank() | username.isEmpty()){
            throw new IllegalArgumentException("The username is empty");
        }
        if(password.isEmpty() | password.isBlank()){
            throw new IllegalArgumentException("No password has been typed in");
        }
    }

    //Login-------------------------------------------------------------------------------------------------------------
    public boolean verifyLogin(String username, String password) throws Exception {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("Username cannot be empty");
            }
            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("Password connot be empty");
            }
            return operatorRepo.login(username, password);
    }


    // TEST

    public List<Booking> getActiveBookings() {
        System.out.println("GET ACTIVE BOOKINGS ACTIVATED");
        List<Booking> aBookings = new ArrayList<>();
        LocalDateTime lDate = LocalDateTime.of(2026, 02, 27, 14, 30);
        aBookings.add(new Booking(1, "Jonas", "23907290", "Hej@gmail.com",45,lDate, "Betina", true, TreatmentType.MandCut ));
        return aBookings;
    }
}
