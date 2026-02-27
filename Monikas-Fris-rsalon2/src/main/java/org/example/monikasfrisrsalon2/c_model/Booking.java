package org.example.monikasfrisrsalon2.c_model;

import java.sql.Timestamp;

public class Booking {

    private String name;
    private String tlfNumber;
    private String email;
    private Timestamp availability;
    private Hairdresser hairdresser;
    private TreatmentType treatment;

    public Booking(String name,
                   String tlfNumber,
                   String email,
                   Timestamp availability,
                   Hairdresser hairdresser,
                   TreatmentType treatment) {

        this.name = name;
        this.tlfNumber = tlfNumber;
        this.email = email;
        this.availability = availability;
        this.hairdresser = hairdresser;
        this.treatment = treatment;
    }

    // Getters

    public String getName() { return name; }
    public String getTlfNumber() { return tlfNumber; }
    public String getEmail() { return email; }
    public Timestamp getAvailability() { return availability; }
    public Hairdresser getHairdresser() { return hairdresser; }
    public TreatmentType getTreatment() { return treatment; }
}