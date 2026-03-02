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

    public List<Hairdresser> loadHairdressers() throws SQLException {
        List<Hairdresser> hairdressers = new ArrayList<>();
        String sql = "SELECT * FROM Hairdresser";
        try (Connection conn = DbConnect.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Hairdresser hairdresser = createHairdresserFromResultset(rs);
                hairdressers.add(hairdresser);
            }
        } catch (SQLException e) {
            throw new RuntimeException("failed to initialize customers");
        }
        return hairdressers;
    }
    private Hairdresser createHairdresserFromResultset(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String phoneNumber = rs.getString("phoneNumber");

        return new Hairdresser(id, name, email, phoneNumber);
    }
}
