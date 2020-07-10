package org.example.jdbc.courses;

import org.example.jdbc.starter.ConnectionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CourseManagerTest {

    private CourseManager underTest;
    private ConnectionFactory connectionFactory;
    private static final String DATABASE_FILE_NAME = "test-database.properties";

    @Before
    public void setUp() throws Exception {
        underTest = new CourseManager();
        connectionFactory = new ConnectionFactory(DATABASE_FILE_NAME);
    }

    @Test
    public void testInsertStudent() throws SQLException {
        String countStudentsQuery = "SELECT COUNT(*) FROM students;";
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet studentsNumResultSet = statement.executeQuery(countStudentsQuery);
            studentsNumResultSet.next();
            int studentsNumBefore = studentsNumResultSet.getInt(1);
            underTest.insertStudent("Jan Kowalski", 1, "Aktualnie fryzjer", "17.C");
            studentsNumResultSet = statement.executeQuery(countStudentsQuery);
            studentsNumResultSet.next();
            int studentsNumAfter = studentsNumResultSet.getInt(1);
            Assert.assertEquals(studentsNumBefore, studentsNumAfter - 1);
        }
    }
}
