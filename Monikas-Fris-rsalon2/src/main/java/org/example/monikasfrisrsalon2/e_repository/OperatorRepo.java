package org.example.monikasfrisrsalon2.e_repository;

import org.example.monikasfrisrsalon2.c_model.Operator;
import org.example.monikasfrisrsalon2.d_dbconfig.DbConnect;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OperatorRepo {
    public void createOperator(Operator operator) throws Exception {

        String hashedPassword = BCrypt.hashpw(operator.getPassword(), BCrypt.gensalt());

        String sql = "INSERT INTO operator (username, password_hash) VALUES (?, ?)";

        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, operator.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
        }
    }

    public boolean login(Operator operator) throws Exception {
        String sql = "SELECT password_hash FROM operator WHERE username = ?";

        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, operator.getUsername());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");

                return (BCrypt.checkpw(storedHash, operator.getPassword()));
            }
            return false;
        }
    }
}

