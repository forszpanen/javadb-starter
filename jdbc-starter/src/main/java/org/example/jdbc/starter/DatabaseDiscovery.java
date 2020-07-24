package org.example.jdbc.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseDiscovery {
    private static Logger logger = LoggerFactory.getLogger(DatabaseDiscovery.class);

    public static void main(String[] args) throws SQLException {
        try(Connection connection = new ConnectionFactory("database.properties").getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            logger.info("RDBMS name {}", metaData.getDatabaseProductName());
            logger.info("RDBMS version: {}", metaData.getDatabaseProductVersion());
            logger.info("Driver name: {}", metaData.getDriverName());
            logger.info("Driver version: {}", metaData.getDriverVersion());
            logger.info("User name: {}", metaData.getUserName());

            ResultSet resultSet = metaData.getColumns("reading_room", null, "books", null);
            while(resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                String columnName = resultSet.getString("COLUMN_NAME");
                String dataType = resultSet.getString("DATA_TYPE");
                String columnSize = resultSet.getString("COLUMN_SIZE");
                logger.info("table: {}, columnName: {}, dataType: {}, columnSize: {}", tableName, columnName, dataType, columnSize);
            }
        }
    }

}
