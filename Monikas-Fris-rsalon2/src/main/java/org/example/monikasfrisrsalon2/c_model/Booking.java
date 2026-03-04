package org.example.monikasfrisrsalon2.c_model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Booking {
    private int id;
    private String name;
    private String phoneNumber;
    private String email;
    private int duration;
    private LocalDateTime BookingTime;
    private String hairdresserName;
    private TreatmentType treatment;
    private Status status;



    public Booking(int id, String name, String phoneNumber, String email,TreatmentType treatment, int duration, LocalDateTime dateTime,
                   String hairdresserName,Status status) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.treatment = treatment;
        this.duration = duration;
        this.hairdresserName = hairdresserName;
        this.BookingTime = dateTime;
        this.status = status;
    }
    public int getId() {
        return id;
    }
    public LocalDateTime getDateTime() { return BookingTime; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getHairdresser() { return hairdresserName; }
    public TreatmentType getTreatment() { return treatment; }
}