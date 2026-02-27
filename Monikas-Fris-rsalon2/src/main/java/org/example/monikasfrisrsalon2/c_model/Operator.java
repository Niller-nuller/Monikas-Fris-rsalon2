package org.example.monikasfrisrsalon2.c_model;

public class Operator {
    private int id;
    private String username;
    private String password;

    public Operator(int id, String username){
        this.id = id;
        this.username = username;
    }
    public Operator(String username,String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
}
