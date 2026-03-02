package org.example.monikasfrisrsalon2.e_repository;

import org.example.monikasfrisrsalon2.c_model.*;
import org.example.monikasfrisrsalon2.d_dbconfig.DbConnect;

import java.sql.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingRepo {

    public List<Booking> loadAllBookings() throws SQLException {
        List<Booking> Bookings = new ArrayList<>();
        String sql = """
            
                SELECT
                        b.id,
                        c.name AS customer_name,
                        c.phoneNumber AS customer_phonenumber,
                        c.email AS customer_email,
                        t.durationMinutes AS treatment_duration,
                        b.due_at,
                        b.created_at,
                        h.id AS hairdresser_id,
                        h.name AS hairdresser_name,
                        h.email AS hairdresser_email,
                        h.phoneNumber AS hairdresser_phone,
                        b.availability,
                        t.type AS treatment_type
                        FROM bookings b
                        JOIN customers c ON b.customer_id = c.id
                        JOIN hairdressers h ON b.hairdresser_id = h.id
                        JOIN treatments t ON b.treatment_id = t.id
                        ORDER BY b.due_at
            """;

        try (Connection conn = DbConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking booking = createBookingFromResult(rs);
                   Bookings.add(booking);

            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to initialize booking");
        }
        return Bookings;
    }

    private Booking createBookingFromResult(ResultSet rs) throws SQLException {
       int id = rs.getInt("id");
       String name = rs.getString("customer_name");
       String phoneNumber = rs.getString("customer_phonenumber");
       String email = rs.getString("customer_email");
       int duration = rs.getInt("treatment_duration");
       LocalDateTime dateTime = rs.getTimestamp("due_at").toLocalDateTime();
       String hairdresserName = rs.getString("hairdresser_name");
       boolean availability = rs.getBoolean("availability");
       TreatmentType treatment = TreatmentType.valueOf(rs.getString("treatment_type"));

       return new Booking(id,name,phoneNumber,email,duration,dateTime,hairdresserName,availability,treatment);
    }

    public void createABooking(Customer customer, Hairdresser hairdresser, Treatment treatment, LocalDateTime startTime) throws SQLException {
        int duration = treatment.getDurationMinutes();
        LocalDateTime endTime = startTime.plusMinutes(duration);
        String SQL = "INSERT INTO bookings (customer_id, hairdresser_id, treatment_id, start_time, end_time) VALUES (?,?,?,?,?)";
        try(Connection conn = DbConnect.getConnection()){
            try(PreparedStatement ps = conn.prepareStatement(SQL);){
                ps.setInt(1,customer.getId());
                ps.setInt(2,hairdresser.getId());
                ps.setInt(3,treatment.getId());
                ps.setTimestamp(4, Timestamp.valueOf(startTime));
                ps.setTimestamp(5,Timestamp.valueOf(endTime));
                ps.executeQuery();
            }
        }
    }
}
