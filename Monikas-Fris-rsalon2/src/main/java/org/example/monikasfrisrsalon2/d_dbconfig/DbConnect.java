package org.example.monikasfrisrsalon2.d_dbconfig;

import java.io.FileInputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnect{
    public static Connection getConnection() throws SQLException{
        Properties prop = new Properties();
        try (Reader r = Files.newBufferedReader(Path.of(".env"), StandardCharsets.UTF_8)) {
            prop.load(r);
        } catch (Exception e) {
            System.out.println("failed to load .env file: ");
            e.printStackTrace();
            throw new RuntimeException("could not load database connection", e);
        }
        String url = prop.getProperty("url");
        String user = prop.getProperty("user");
        String password = prop.getProperty("password");

        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e){
            System.out.println("failed to connect to database");
            e.printStackTrace();
            throw e;
        }
        return conn;
    }
}
