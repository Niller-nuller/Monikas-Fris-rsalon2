package org.example.monikasfrisrsalon2.e_repository;

import org.example.monikasfrisrsalon2.c_model.Customer;
import org.example.monikasfrisrsalon2.d_dbconfig.DbConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CustomerRepo {
    public List<Customer> initializeCustomer() throws SQLException {
        List<Customer> Customers = new ArrayList<>();
        String sql = "SELECT name, email, id FROM Customer";

        try (Connection conn = DbConnect.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = createCustomerFromResultset(rs);
                if (customer != null) {
                    Customers.add(customer);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to initialize customers", e);
        }
        return Customers;
    }
    private Customer createCustomerFromResultset(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String email = rs.getString("email");
        int id = rs.getInt("id");

        return new Customer(name, email, id);
    }
    public void insertCustomer(String Name, String Email) throws SQLException {
        String sql = "INSERT INTO customer (Name, Email) VALUES (?,?)";
        try(Connection conn = DbConnect.getConnection()){
            try(PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1,Name);
                pstmt.setString(2,Email);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("save failed", e);
        }
    }
}
