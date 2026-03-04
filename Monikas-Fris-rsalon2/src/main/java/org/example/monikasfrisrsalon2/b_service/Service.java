package org.example.monikasfrisrsalon2.b_service;


import org.example.monikasfrisrsalon2.c_model.*;
import org.example.monikasfrisrsalon2.e_repository.*;

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

    List<Hairdresser> hairdressers = new ArrayList<>();
    
    public Service(BookingRepo bookingRepo, CustomerRepo customerRepo, HairDresserRepo hairDresserRepo, TreatmentRepo treatmentRepo, OperatorRepository operatorRepo, ServiceLogin serviceLogin) {
        this.bookingRepo = bookingRepo;
        this.customerRepo = customerRepo;
        this.hairDresserRepo = hairDresserRepo;
        this.treatmentRepo = treatmentRepo;
        this.operatorRepo = operatorRepo;
        this.serviceLogin = serviceLogin;
    }

    public void initialize(){
        getOperator();
        populateHairdressers();
    }
    //--------------- Control----------------------------------------------------------------------------------

    public void populateHairdressers(){
        try{
        hairdressers = hairDresserRepo.loadHairdressers();}catch(Exception e){
            System.out.println("SQL problem");
        }
    }

    public List<Booking> handleGetPendingBookings(LocalDate date) throws SQLException {
        //return bookingRepo.getBookingListBasedOnStatus(Status.Pending, date);
        return null;
    }
    public void cancelBooking(Booking booking) throws SQLException {
        //bookingRepo.chancelBooking(booking);
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

    public void createBooking(Customer customer, Hairdresser hairdresser, TreatmentType treatmentType) throws SQLException {
        LocalDateTime dateTime = LocalDateTime.now();
        try{
        bookingRepo.createABooking(customer, hairdresser, getTreatment(treatmentType), dateTime);} catch(Exception e){
            throw e;
        }
    }

    public Customer createCustomer(int id, String name, String email, String phoneNumber){
        return new Customer(id, name, email, phoneNumber);
    }
    public Treatment getTreatment(TreatmentType treatmentType){
        TreatmentRegistry treatmentRegistry = new TreatmentRegistry();
        Treatment treatment = treatmentRegistry.getDefinition(treatmentType);
        return treatment;
    }
    public LocalDateTime getBookingTime(int year, int month, int day, int hour){
        LocalDateTime startTime = LocalDateTime.of(year, month, day, hour, 0);
        return startTime;
    }

    public Hairdresser getHairdresser(String name){
        for(Hairdresser hairdresser : hairdressers){
            if(hairdresser.getName().equals(name)){
                return hairdresser;
            }
        }
        return null;
    }
}
