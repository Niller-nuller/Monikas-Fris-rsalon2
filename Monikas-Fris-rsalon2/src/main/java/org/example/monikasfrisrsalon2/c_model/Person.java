package org.example.monikasfrisrsalon2.c_model;

public class Person {
    private int id;
    private String name;
    private String email;
    private int phoneNumber;

    public Person(String name, String email, int phoneNumber){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public int getPhoneNumber(){
        return phoneNumber;
    }
}
