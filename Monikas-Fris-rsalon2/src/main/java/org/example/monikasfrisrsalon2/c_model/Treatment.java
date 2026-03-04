package org.example.monikasfrisrsalon2.c_model;

public class Treatment {
    private int id;
    private String name;
    private int durationMinutes;
    private double price;

    public Treatment(int id, String name, int durationMinutes, double price) {
        this.id = id;
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.price = price;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getDurationMinutes() {
        return durationMinutes;
    }
    public double getPrice() {
        return price;
    }
    @Override
    public String toString() {
        return name + " (" + durationMinutes + " min, " + price + " kr)";
    }
}
