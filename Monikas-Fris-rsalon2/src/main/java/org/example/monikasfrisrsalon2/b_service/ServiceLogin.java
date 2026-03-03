package org.example.monikasfrisrsalon2.b_service;


import org.example.monikasfrisrsalon2.c_model.Operator;
import org.example.monikasfrisrsalon2.e_repository.OperatorRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class ServiceLogin {
    private final OperatorRepository operatorRepo;

    Operator currentOperator;
    public ServiceLogin (OperatorRepository operatorRepository){
        this.operatorRepo = operatorRepository;
    }
    //Register--------------------------------------------------------------------------------------------
    public Operator createOperator(String Username, String Password){
        return new Operator(
                Username, Password
        );
    }
    public void createOperator(Operator newOperator) throws SQLException{
        try{
        operatorRepo.registerOperator(newOperator);} catch (SQLException e){
            throw e;
        }
    }
    //login----------------------------------------------------------------------------------------------
    public boolean login(Operator loginRequest) throws SQLException {
        try {
            Operator dbOperator = operatorRepo.loginOperator(loginRequest);
            if( dbOperator != null){
                currentOperator = dbOperator;
                return true;
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }
    //Booking stuff-------------------------------------------------------------------------
    public Operator getOperator(){
        return currentOperator;
    }
}
