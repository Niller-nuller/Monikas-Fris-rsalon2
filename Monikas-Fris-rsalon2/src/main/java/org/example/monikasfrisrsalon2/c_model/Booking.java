package org.example.monikasfrisrsalon2.c_model;

import java.time.format.DateTimeFormatter;

public class Booking {
    private String name;
    private int tlfNumber;
    private String email;
    private DateTimeFormatter dateTime;
    private Hairdresser hairdresserName;
    private Timestamp availability;
    private TreatmentType treatment;

    public Booking(String name, int tlfNumber, DateTimeFormatter dateTime, Hairdresser hairdresserName, Timestamp availability, TreatmentType treatment){
        this.name = name;
        this.tlfNumber = tlfNumber;
        this.email = null;
        this.dateTime = dateTime;
        this.hairdresserName = hairdresserName;
        this.availability = availability;
        this.treatment = treatment;
    }
    public Booking(String name, String email, DateTimeFormatter dateTime, Hairdresser hairdresserName, Timestamp availability, TreatmentType treatment){
        this.name = name;
        this.tlfNumber = Integer.parseInt(null);
        this.email = email;
        this.dateTime = dateTime;
        this.hairdresserName = hairdresserName;
        this.availability = availability;
        this.treatment = treatment;
    }
}
