package org.example.monikasfrisrsalon2.e_repository;

import org.example.monikasfrisrsalon2.c_model.Customer;
import org.example.monikasfrisrsalon2.d_dbconfig.DbConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CustomerRepo {
    public List<Customer> loadCustomer() throws SQLException {
        List<Customer> Customers = new ArrayList<>();
        String sql = "SELECT * FROM customer";

        try (Connection conn = DbConnect.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = createCustomerFromResultset(rs);
                Customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to initialize customers", e);
        }
        return Customers;
    }
    private Customer createCustomerFromResultset(ResultSet rs) throws SQLException {
        int id = rs.getInt("CustomerId");
        String name = rs.getString("Name");
        String email = rs.getString("Email");
        String phoneNumber = rs.getString("PhoneNumber");

        return new Customer(id,name,email,phoneNumber);
    }


    public void insertCustomer(String Name, String Email,String phoneNumber){
        String sql = "INSERT INTO customer (Name, Email, PhoneNumber) VALUES (?,?,?)";
        try(Connection conn = DbConnect.getConnection()){
            try(PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setString(1,Name);
                stmt.setString(2,Email);
                stmt.setString(3,phoneNumber);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("save failed", e);
        }
    }

    ///  NEW

    public Customer upsertCustomer(String name, String email, String phone) throws SQLException {

        String findSql = "SELECT CustomerId FROM customer WHERE Email = ? OR PhoneNumber = ? LIMIT 1";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(findSql)) {
            ps.setString(1, email);
            ps.setString(2, phone);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("CustomerId");
                    return new Customer(id, name, email, phone);
                }
            }
        }


        String insertSql = "INSERT INTO customer (Name, Email, PhoneNumber) VALUES (?,?,?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Customer(keys.getInt(1), name, email, phone);
                }
            }
        }

        throw new SQLException("Could not insert customer");
    }
}
