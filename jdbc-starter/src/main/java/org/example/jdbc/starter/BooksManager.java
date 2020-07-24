package org.example.jdbc.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BooksManager {
    private static final Logger logger = LoggerFactory.getLogger(BooksManager.class);

    public static void main(String[] args) {
        try (Connection connection = new ConnectionFactory("database.properties").getConnection()) {
            //addBook(connection);
            listNonReservedBooks(connection);
            //listBooksWithCategories(connection);
            //updateNettoPrice(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listNonReservedBooks(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books WHERE reserved = 0;");
            while (resultSet.next()) {
                logger.info("title {}", resultSet.getString("title"));
            }
        }
    }

    private static void addBook(Connection connection) throws SQLException {
        boolean defaultAutocommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement()) {
            int affectedRows = statement.executeUpdate("insert into books(title, publish_date, ISBN, pages, publisher, netto_price, vat_rate, reserved) " +
                    "values ('Linux. Komendy i polecenia. Wydanie IV rozszerzone', '2014-06-13', '978-83-246-8838-8', 176, 'Helion', 16.18, 5, 0);");
            logger.info("Created {} rows in books", affectedRows);
            affectedRows = statement.executeUpdate("insert into authors(first_name, last_name) values('Łukasz', 'Sosna');");
            logger.info("Created {} rows in authors", affectedRows);

            ResultSet latestBookIdResultSet = statement.executeQuery("select book_id from books order by book_id desc limit 0,1;");
            latestBookIdResultSet.next();
            int bookId = latestBookIdResultSet.getInt("book_id");
            ResultSet latestAuthorIdResultSet = statement.executeQuery("select author_id from authors order by author_id desc limit 0,1;");
            latestAuthorIdResultSet.next();
            int authorId = latestAuthorIdResultSet.getInt("author_id");
            affectedRows = statement.executeUpdate("insert into books_authors(book_id, author_id) values (" + bookId + "," + authorId + ");");
            logger.info("Created {} rows in books_authors", affectedRows);

            int createdCategories = statement.executeUpdate("insert into categories(name) values ('Sytemy operacyjne')");
            logger.info("Created {} category", createdCategories);
            statement.executeUpdate("insert into books_categories(book_id, category_id) values (" + bookId + ", (select category_id from categories where name='Systemy operacyjne'));");
            createdCategories = statement.executeUpdate("insert into categories(name) values ('Linux')");
            statement.executeUpdate("insert into books_categories(book_id, category_id) values (" + bookId + ", (select category_id from categories where name='Linux'));");
            logger.info("Created {} category", createdCategories);
        }
        connection.setAutoCommit(defaultAutocommit);
    }

    private static void listBooksWithCategories(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet booksWithCategories = statement.executeQuery("SELECT b.title, c.name from books b " +
                    "left join books_categories bc on b.book_id = bc.book_id left join categories c on bc.category_id = c.category_id;");
            while (booksWithCategories.next()) {
                logger.info("title {}, category {}", booksWithCategories.getString("b.title"), booksWithCategories.getString("c.name"));
            }
        }
    }

    private static void updateNettoPrice(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int updatedBooks = statement.executeUpdate("update books set netto_price = netto_price * 1.2 where title = 'Linux. Komendy i polecenia. Wydanie IV rozszerzone';");
            logger.info("Updated books {}", updatedBooks);
        }
    }
}
