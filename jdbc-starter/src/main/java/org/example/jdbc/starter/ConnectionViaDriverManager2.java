package org.example.jdbc.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionViaDriverManager2 {
    private static Logger logger = LoggerFactory.getLogger(ConnectionViaDriverManager2.class);

    private static final String DB_URL = "jdbc:mysql://localhost:3306/reading_room?useSSL=false&serverTimezone=Europe/Warsaw";
    private static final String DB_USER = "reading_room_admin";
    private static final String DB_PASSWORD = "reading_room_admin";

    public static void main(String[] args) {
        logger.info("Connecting to a selected database...");
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
            logger.info("Connected database successfully...");
            logger.info("Connection = " + connection);
            logger.info("Database name = " + connection.getCatalog());
        } catch (SQLException e) {
            logger.error("Error during using connection", e);
        }
    }
}