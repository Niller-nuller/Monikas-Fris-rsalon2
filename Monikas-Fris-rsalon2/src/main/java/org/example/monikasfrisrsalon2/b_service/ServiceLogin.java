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
    public boolean login(Operator loginRequest) throws SQLException {
        Operator dbOperator = operatorRepo.findByUsername(loginRequest.getUsername());
        if (dbOperator != null && BCrypt.checkpw(loginRequest.getPassword(), dbOperator.getPassword())) {
            dbOperator.setPassword(null);
            this.currentOperator =  dbOperator;
            return true;
        }
        return false;
    }
    public void logOut(Operator operator) throws SQLException {
        operator.setPassword(null);
    }

    public Operator getOperator(){
        return currentOperator;
    }

}
