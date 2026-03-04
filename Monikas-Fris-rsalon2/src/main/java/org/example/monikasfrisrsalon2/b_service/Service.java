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
        hairdressers = hairDresserRepo.loadHairdressers();}
        catch(Exception e){
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
        return aBookings;
    }
    
    public void getOperator(){
        this.currentOperator = serviceLogin.getOperator();
    }

    public Customer createCustomer(int id, String name, String email, String phoneNumber){
        return new Customer(id, name, email, phoneNumber);
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

    ///  NEW

    public java.util.List<Treatment> getTreatments() throws SQLException {
        return treatmentRepo.loadTreatments();
    }

    public java.util.List<Hairdresser> getEligibleHairdressers(Treatment treatment) throws SQLException {
        return hairDresserRepo.loadHairdressers();
    }

    public record TimeSlot(LocalDateTime start, LocalDateTime end, boolean available) {}

    public java.util.List<TimeSlot> getTimeSlots(java.time.LocalDate date, Hairdresser hairdresser, Treatment treatment) throws SQLException {
        var booked = bookingRepo.getBookedIntervals(hairdresser.getId(), date);

        LocalDateTime open = date.atTime(9, 0);
        LocalDateTime close = date.atTime(17, 0);

        int duration = treatment.getDurationMinutes();
        int step = 15;

        java.util.List<TimeSlot> slots = new java.util.ArrayList<>();

        for (LocalDateTime start = open; !start.plusMinutes(duration).isAfter(close); start = start.plusMinutes(step)) {
            LocalDateTime end = start.plusMinutes(duration);

            LocalDateTime finalStart = start;
            boolean overlaps = booked.stream().anyMatch(b ->
                    finalStart.isBefore(b.end()) && end.isAfter(b.start())
            );

            slots.add(new TimeSlot(start, end, !overlaps));
        }
        return slots;
    }

    public void createBooking(String name, String email, String phone,
                              Treatment treatment, Hairdresser hairdresser, LocalDateTime start) throws SQLException {
        Customer customer = customerRepo.upsertCustomer(name, email, phone);
        bookingRepo.createABooking(customer, hairdresser, treatment, start);
    }
}
