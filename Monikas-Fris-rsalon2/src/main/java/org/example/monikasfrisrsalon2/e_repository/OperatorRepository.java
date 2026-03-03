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

        String sql = "insert into operators (username, password_hash, role) values (?, ?, ?)";


        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, operator.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, "role");
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }

    }
    public Operator loginOperator (Operator loginRequest) throws SQLException {
        String sql = "select username, role from operators where username = ? and password_hash IS NOT NULL";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loginRequest.getUsername());
            stmt.setString(2, loginRequest.getPassword());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Operator(
                        rs.getString("username"),
                        null,
                        rs.getString("role")
                );
            }
        }
        return null;
    }
    public Operator findByUsername (String username) throws SQLException {
        String sql = "select id , username, password_hash, role from operators where username = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Operator(
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("role")
                );
            }
        }
        return null;
    }
}