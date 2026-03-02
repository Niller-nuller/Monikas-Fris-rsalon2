package org.example.monikasfrisrsalon2.e_repository;

import org.example.monikasfrisrsalon2.c_model.*;
import org.example.monikasfrisrsalon2.d_dbconfig.DbConnect;

import java.sql.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingRepo {

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
    public List<Booking> getBookingListBasedOnStatus(Status status, LocalDate date) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String SQL = """
                 SELECT
                b.id,
                c.name AS name,
                c.phonenumber AS phoneNumber
                c.email AS email
                t.type AS treatmentType,
                t.duration AS treatmentDuration,
                e.name AS hairdresserName,
                b.dateTime,
                b.status
                FROM bookings b
                LEFT JOIN customers c ON b.customerId = c.id
                LEFT JOIN employees e ON b.employeeId = e.id \s
                LEFT JOIN treatments t ON b.treatmentId = t.id
                WHERE b.status = ?
                AND date(dateTime) = ?
                ORDER BY b.dateTime""";

        try(Connection conn = DbConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setString(1,status.toString());
            ps.setDate(2,java.sql.Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Booking booking = createBookingFromRS(rs);
                bookings.add(booking);
            }
        }
        return bookings;
    }
    private Booking createBookingFromRS(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String customerName = rs.getString("customerName");
        String phoneNumber = rs.getString("phonenumber");
        String email = rs.getString("email");
        TreatmentType treatment = TreatmentType.valueOf(rs.getString("treatment_type"));
        int duration = rs.getInt("duration_minutes");
        LocalDateTime dateTime = rs.getTimestamp("start_time").toLocalDateTime();
        String hairdresserName = rs.getString("employee_name");
        Status status = Status.valueOf(rs.getString("status"));
        return new Booking(id,customerName, phoneNumber,email,treatment, duration,dateTime,hairdresserName,status);
    }
    public void chancelBooking(Booking booking) throws SQLException {
        String SQL = "UPDATE bookings SET status = 'Cancelled' WHERE id = ?";
        try(Connection conn = DbConnect.getConnection()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1,booking.getId());
            ps.executeQuery();
        }
    }
}
