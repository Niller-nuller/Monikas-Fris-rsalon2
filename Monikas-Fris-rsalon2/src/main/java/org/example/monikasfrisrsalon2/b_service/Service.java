package org.example.monikasfrisrsalon2.b_service;

import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import org.example.monikasfrisrsalon2.c_model.*;
import org.example.monikasfrisrsalon2.e_repository.*;

import java.sql.SQLException;
import java.util.*;

public class Service {
private final BookingRepo bookingRepo;
private final CustomerRepo customerRepo;
private final HairDresserRepo hairDresserRepo;
private final TreatmentRepo treatmentRepo;
private final OperatorRepo operatorRepo;

private Operator operator_in_use;

    public Service(BookingRepo bookingRepo,CustomerRepo customerRepo,HairDresserRepo hairDresserRepo,TreatmentRepo treatmentRepo, OperatorRepo operatorRepo) throws SQLException {
        this.bookingRepo = bookingRepo;
        this.customerRepo = customerRepo;
        this.hairDresserRepo = hairDresserRepo;
        this.treatmentRepo = treatmentRepo;
        this.operatorRepo = operatorRepo;
        populateTreatmentRegistry();
    }
    private final Map<TreatmentType, Treatment> treatmentRegistry = new HashMap<>();
    private final List<Customer> customerRegistry = new ArrayList<>();
    private final List<Hairdresser> hairdresserRegistry = new ArrayList<>();
    private final List<Booking> bookingRegistry = new ArrayList<>();

    //---------------Treatment Control----------------------------------------------------------------------------------

    private void populateTreatmentRegistry() throws SQLException {
        for(Treatment t : treatmentRepo.initializeTreatment()){
            treatmentRegistry.put(t.getType(), t);
        }
    }
    public Treatment getTreatment(TreatmentType type){
        return treatmentRegistry.get(type);
    }
    public List<Treatment> getAllTreatments(){
        return new ArrayList<>(treatmentRegistry.values());
    }

//----------------Customer Control--------------------------------------------------------------------------------------
    public void populateCustomerRegistry() throws SQLException {
        customerRegistry.addAll(customerRepo.initializeCustomer());
    }
    public Customer getCustomer(String name){
        for(Customer c : customerRegistry){
            if(Objects.equals(name, c.getName())){
                return c;
            }
        }
        return null;
    }
    public List<Customer> getCustomerList(){
        return customerRegistry;
    }

//--------------Hairdresser Control-------------------------------------------------------------------------------------

    public void populateHairdresserRegistry() throws SQLException {
        hairdresserRegistry.addAll(hairDresserRepo.initializeHairDresser());
    }
    public Hairdresser getHairdresser(String name){
        for(Hairdresser h : hairdresserRegistry){
            if(h.getName().equals(name)){
                return h;
            }
        }
        return null;
    }
    public List<Hairdresser> getHairdresserList(){
        return hairdresserRegistry;
    }

//---------------Booking Control----------------------------------------------------------------------------------------

    public void populateBookingRegistry() throws SQLException {
        bookingRegistry.addAll(bookingRepo.initializeBooking());
    }


    //Login-------------------------------------------------------------------------------------------------------------

    public boolean verifyLogin(String username, String password){
        for (Operator operator : operatorRepo.initializeOperators()){
            if (username.equals(operator.getUsername()) && password.equals((operator.getPassword()))){
                operator = operator_in_use;
                return true;
            }
        }
        return false;
    }
}
