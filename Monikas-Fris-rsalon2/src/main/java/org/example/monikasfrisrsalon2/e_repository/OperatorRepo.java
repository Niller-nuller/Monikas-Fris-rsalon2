package org.example.monikasfrisrsalon2.e_repository;

import org.example.monikasfrisrsalon2.c_model.Operator;
import org.example.monikasfrisrsalon2.d_dbconfig.DbConnect;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OperatorRepo {
    public void createOperator(String username, String password) throws SQLException {

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String sql = "INSERT INTO operator (username, password_hash) VALUES (?, ?)";

        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
        }
    }

    public boolean login(String username, String password) throws SQLException {
        String sql = "SELECT password_hash FROM operator WHERE username = ?";

        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");

                return BCrypt.checkpw(password, storedHash);
            }
            return false;
        }
    }
    public boolean authenticateOperator(Operator operator) throws SQLException {
        String SQL = "SELECT password_hash FROM operators WHERE username = ?";
        try(Connection conn = DbConnect.getConnection()){
            PreparedStatement ps = conn.prepareStatement(SQL); {
                ps.setString(1,operator.getUsername());
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    String comparePass = rs.getString("password_hash");

                    return BCrypt.checkpw(operator.getPassword(), comparePass);
                }
                throw new IllegalArgumentException("The username does not exist");
            }
        }
    }
}