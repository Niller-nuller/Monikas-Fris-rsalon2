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
    public List<Customer> loadCustomer() throws SQLException {
        List<Customer> Customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";

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
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String phoneNumber = rs.getString("phonenumber");

        return new Customer(id,name,email,phoneNumber);
    }


    public void insertCustomer(String Name, String Email,String phoneNumber){
        String sql = "INSERT INTO customer (Name, Email, phoneNumber) VALUES (?,?,?)";
        try(Connection conn = DbConnect.getConnection()){
            try(PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setString(1,Name);
                stmt.setString(2,Email);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("save failed", e);
        }
    }
}
