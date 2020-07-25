package org.example.library;

import org.example.jdbc.starter.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DATABASE_PROPERTIES_FILE = "library-database.properties";
    private ConnectionFactory connectionFactory;

    public DatabaseManager() {
        this.connectionFactory = new ConnectionFactory(DATABASE_PROPERTIES_FILE);
    }

    public void createDb() throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS library.users (\n" +
                    "  id INT NOT NULL AUTO_INCREMENT,\n" +
                    "  login VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_polish_ci NOT NULL,\n" +
                    "  password VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_polish_ci NOT NULL,\n" +
                    "  name VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_polish_ci NOT NULL,\n" +
                    "  admin BOOLEAN NOT NULL,\n" +
                    "PRIMARY KEY (id))\n" +
                    "  ENGINE = InnoDB;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS library.categories (\n" +
                    "  id INT NOT NULL AUTO_INCREMENT,\n" +
                    "  name VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_polish_ci NOT NULL,\n" +
                    "PRIMARY KEY (id))\n" +
                    "  ENGINE = InnoDB;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS library.books (\n" +
                    "  id INT NOT NULL AUTO_INCREMENT,\n" +
                    "  category_id INT NOT NULL,\n" +
                    "  title VARCHAR(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_polish_ci NOT NULL,\n" +
                    "  author VARCHAR(70) CHARACTER SET utf8mb4 COLLATE utf8mb4_polish_ci NOT NULL,\n" +
                    "PRIMARY KEY (id),\n" +
                    "FOREIGN KEY (category_id)\n" +
                    "REFERENCES library.categories(id)\n" +
                    "ON DELETE NO ACTION\n" +
                    "ON UPDATE NO ACTION)\n" +
                    "  ENGINE = InnoDB;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS library.orders (\n" +
                    "  id INT NOT NULL AUTO_INCREMENT,\n" +
                    "  user_id INT NOT NULL,\n" +
                    "  book_id INT NOT NULL,\n" +
                    "  order_date DATETIME NOT NULL,\n" +
                    "  return_date DATETIME NULL,\n" +
                    "PRIMARY KEY (id),\n" +
                    "FOREIGN KEY (user_id)\n" +
                    "REFERENCES library.users(id)\n" +
                    "ON DELETE NO ACTION\n" +
                    "ON UPDATE NO ACTION,\n" +
                    "FOREIGN KEY (book_id)\n" +
                    "REFERENCES library.books(id)\n" +
                    "ON DELETE NO ACTION\n" +
                    "ON UPDATE NO ACTION)\n" +
                    "  ENGINE = InnoDB;");
        }

    }

    public void initializeDb() throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO categories (name) values ('Systemy operacyjne');");
        }
    }

    public void dropDb() throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE library.orders;");
            statement.executeUpdate("DROP TABLE library.books;");
            statement.executeUpdate("DROP TABLE library.users;");
            statement.executeUpdate("DROP TABLE library.categories;");

        }
    }


    public static void main(String[] args) throws SQLException {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.createDb();

        //databaseManager.dropDb();
    }
}
