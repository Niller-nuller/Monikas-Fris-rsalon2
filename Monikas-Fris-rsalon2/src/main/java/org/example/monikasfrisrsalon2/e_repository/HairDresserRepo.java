package org.example.monikasfrisrsalon2.e_repository;

import org.example.monikasfrisrsalon2.c_model.Hairdresser;
import org.example.monikasfrisrsalon2.d_dbconfig.DbConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HairDresserRepo {
    public List<Hairdresser> initializeHairDresser() throws SQLException {
        List<Hairdresser> haridressers = new ArrayList<>();
        String sql = "SELECT id, name, time FROM Hairdresser"; //Time????
        try (Connection conn = DbConnect.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Hairdresser hairdresser = createHairdresserFromResultset(rs);
                haridressers.add(hairdresser);
            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to initialize customers");
        }
        return haridressers;
    }
    private Hairdresser createHairdresserFromResultset(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int time = rs.getInt("time");

        return new Hairdresser(id, name, time);
    }
}
