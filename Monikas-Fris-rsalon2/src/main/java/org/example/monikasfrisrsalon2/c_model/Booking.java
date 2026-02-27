package org.example.monikasfrisrsalon2.c_model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Booking {
    private int id;
    private String name;
    private String phoneNumber;
    private String email;
    private int duration;
    private LocalDateTime dateTime;
    private String hairdresserName;
    private boolean availability;
    private TreatmentType treatment;



    public Booking(int id, String name, String phoneNumber, String email, int duration, LocalDateTime dateTime,
                   String hairdresserName, boolean availability, TreatmentType treatment) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.duration = duration;
        this.hairdresserName = hairdresserName;
        this.dateTime = dateTime;
        this.availability = availability;
        this.treatment = treatment;
    }

    public LocalDateTime getDateTime() { return dateTime; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public boolean getAvailability() { return availability; }
    public String getHairdresser() { return hairdresserName; }
    public TreatmentType getTreatment() { return treatment; }
}