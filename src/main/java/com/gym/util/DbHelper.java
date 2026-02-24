package com.gym.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/gym_system?serverTimezone=Asia/Taipei";
    private static final String USER = "root";
    private static final String PASS = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
