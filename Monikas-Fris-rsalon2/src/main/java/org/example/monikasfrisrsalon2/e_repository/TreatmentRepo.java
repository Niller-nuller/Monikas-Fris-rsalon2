package org.example.monikasfrisrsalon2.e_repository;

import org.example.monikasfrisrsalon2.c_model.Treatment;
import org.example.monikasfrisrsalon2.d_dbconfig.DbConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TreatmentRepo {
    public List<Treatment> loadTreatments() throws SQLException {
        List<Treatment> treatments = new ArrayList<>();
        String sql = "SELECT * FROM treatment";

        try(Connection conn = DbConnect.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Treatment treatment = createTreatmentFromResult(rs);
                treatments.add(treatment);
            }
        }catch (SQLException e) {
            throw new RuntimeException("failed to initialize treatment", e);
        }
        return treatments;

    }

    private Treatment createTreatmentFromResult(ResultSet rs) throws SQLException {
        int id = rs.getInt("TreatmentId");
        String name = rs.getString("Name");
        int durationMinutes = rs.getInt("DurationMinutes");
        double price = rs.getDouble("Price");

        return new Treatment(id, name, durationMinutes, price);

    }

}
