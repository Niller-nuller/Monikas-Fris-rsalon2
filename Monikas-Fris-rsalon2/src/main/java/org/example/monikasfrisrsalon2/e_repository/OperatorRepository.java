package org.example.monikasfrisrsalon2.e_repository;

import org.example.monikasfrisrsalon2.c_model.Operator;
import org.example.monikasfrisrsalon2.d_dbconfig.DbConnect;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OperatorRepository {

    public void registerOperator (Operator operator) throws SQLException {

        String hashedPassword = BCrypt.hashpw(operator.getPassword(), BCrypt.gensalt());

        String sql = "insert into operator (Username, PasswordHash, Role) values (?, ?, ?)";


        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, operator.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, operator.getRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }

    }
    public Operator loginOperator (Operator loginRequest) throws SQLException {
        String sql = "select OperatorId, Username, PasswordHash, Role from operator where Username = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loginRequest.getUsername());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("PasswordHash");
                if (BCrypt.checkpw(loginRequest.getPassword(), storedHash)) {

                    return new Operator(
                            rs.getInt("OperatorId"),
                            rs.getString("Username"),
                            null,
                            rs.getString("Role")
                    );
                }
            }
            return null;
        }
    }
    public Operator findByUsername (String username) throws SQLException {
        String sql = "select OperatorId, Username, PasswordHash, Role from operator where Username = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Operator(
                        rs.getInt("OperatorId"),
                        rs.getString("Username"),
                        rs.getString("PasswordHash"),
                        rs.getString("Role")
                );
            }
        }
        return null;
    }
}