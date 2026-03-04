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

    public List<Booking> loadAllBookings() throws SQLException {
        List<Booking> Bookings = new ArrayList<>();
        String sql = """
            
                SELECT
                        b.BookingId,
                        c.Name AS customer_name,
                        c.Email AS customer_email,
                        c.PhoneNumber AS customer_phoneNumber,
                        t.DurationMinutes AS treatment_duration,
                        b.StartTime,
                        b.EndTime,
                        h.HairdresserId,
                        h.Name AS hairdresser_name,
                        h.Email AS hairdresser_email,
                        h.PhoneNumber AS hairdresser_phone,
                        b.Status,
                        t.Name AS treatment_name
                FROM booking b
                LEFT JOIN customer c ON b.CustomerId = c.CustomerId
                LEFT JOIN hairdresser h ON b.HairdresserId = h.HairDresserId
                LEFT Join treatment t ON b.TreatmentId = t.TreatmentId
                ORDER BY b.StartTime
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
    public List<Booking> getBookingListBasedOnStatus(Status status, LocalDate date) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String SQL = """
                SELECT b.BookingId, c.Name AS customerName, t.Name AS treatmentName,
                t.DurationMinutes AS treatmentDuration, e.Name AS employeeName,
                b.StartTime, b.Status
                FROM Booking b          -- Changed from 'bookings'
                LEFT JOIN Customer c ON b.CustomerId = c.CustomerId     -- Changed casing
                LEFT JOIN Employee e ON b.EmployeeId = e.EmployeeId     -- Changed casing \s
                LEFT JOIN Treatment t ON b.TreatmentId = t.TreatmentId  -- Changed casing
                WHERE b.Status = ? AND DATE(b.StartTime) = ?
                ORDER BY b.StartTime""";
        try (Connection conn = DbConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setString(1, status.toString());
            ps.setDate(2, java.sql.Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking booking = createBookingFromResult(rs);
                bookings.add(booking);
            }
        }
        return bookings;
    }
    private Booking createBookingFromResult(ResultSet rs) throws SQLException {
        int id = rs.getInt("BookingId");
        String customerName = rs.getString("customer_name");
        String customerPhone = rs.getString("customer_phoneNumber");
        String customerEmail = rs.getString("customer_email");
        int duration = rs.getInt("treatment_duration");
        LocalDateTime startTime = rs.getTimestamp("StartTime").toLocalDateTime();
        LocalDateTime endTime = rs.getTimestamp("EndTime").toLocalDateTime();
        String hairdresserName = rs.getString("hairdresser_name");
        String treatmentName = rs.getString("treatment_name");
        Status status = Status.valueOf(rs.getString("Status"));

        return new Booking(id, customerName, customerPhone, customerEmail, treatmentName, duration, endTime, hairdresserName, status);
    }


    // BookingRepo.java
    public void createABooking(Customer customer, Hairdresser hairdresser, Treatment treatment,
                               LocalDateTime startTime, int operatorId) throws SQLException {

        int duration = treatment.getDurationMinutes();
        LocalDateTime endTime = startTime.plusMinutes(duration);

        String sql = """
        INSERT INTO booking (OperatorId, CustomerId, HairdresserId, TreatmentId, StartTime, EndTime, Status)
        VALUES (?,?,?,?,?,?,?)
    """;

        try (Connection conn = DbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, operatorId);
            ps.setInt(2, customer.getId());
            ps.setInt(3, hairdresser.getId());
            ps.setInt(4, treatment.getId());
            ps.setTimestamp(5, Timestamp.valueOf(startTime));
            ps.setTimestamp(6, Timestamp.valueOf(endTime));
            ps.setString(7, "Pending");

            ps.executeUpdate();
        }
    }


    public record BookedInterval(LocalDateTime start, LocalDateTime end) {}

    public List<BookedInterval> getBookedIntervals(int hairdresserId, LocalDate date) throws SQLException {
        String sql = """
            SELECT StartTime, EndTime
            FROM booking
            WHERE HairdresserId = ?
              AND DATE(StartTime) = ?
        """;

        List<BookedInterval> out = new ArrayList<>();

        try (Connection conn = DbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hairdresserId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime start = rs.getTimestamp("StartTime").toLocalDateTime();
                    LocalDateTime end = rs.getTimestamp("EndTime").toLocalDateTime();
                    out.add(new BookedInterval(start, end));
                }
            }
        }
        return out;
    }
}
