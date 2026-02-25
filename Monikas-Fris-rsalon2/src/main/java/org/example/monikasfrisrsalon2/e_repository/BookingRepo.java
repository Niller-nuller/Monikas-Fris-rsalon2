package org.example.monikasfrisrsalon2.e_repository;

import org.example.monikasfrisrsalon2.c_model.Booking;
import org.example.monikasfrisrsalon2.d_dbconfig.DbConnect;

import javax.xml.namespace.QName;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingRepo {
    public List<Booking> initializeBooking() throws SQLException {
        List<Booking> Bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";


        Booking booking = null;
        try (Connection conn = DbConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                booking = createBookingFromResult(rs);
                if (booking != null) {
                    Bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to initialize booking");
        }
        return Bookings;
    }

    private Booking createBookingFromResult(ResultSet rs) throws SQLException {
       String name = rs.getString("name");
       int tlf_number = rs.getInt("tlf_number");
       String email = rs.getString("email");




   return null;
    }
}
