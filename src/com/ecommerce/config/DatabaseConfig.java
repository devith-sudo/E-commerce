// DatabaseConfig.java
package com.ecommerce.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    //update JDBC url to include the schema
    private static final String URL = "jdbc:postgresql://localhost:5432/ecommerce?currentSchema=ecommerce";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}