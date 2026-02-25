package org.example.monikasfrisrsalon2.d_dbconfig;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnect{
    public static Connection getConnection() {
        Properties prop = new Properties();
        try (FileInputStream inputStream = new FileInputStream(".env")) {
            prop.load(inputStream);
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
        }
        return conn;
    }
}
