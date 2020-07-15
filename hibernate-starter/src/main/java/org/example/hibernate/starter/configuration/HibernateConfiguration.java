package org.example.hibernate.starter.configuration;

import org.example.hibernate.starter.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateConfiguration {
    private static Logger logger = LoggerFactory.getLogger(HibernateConfiguration.class);

    public static void main(String[] args) {

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Student student = new Student("Jan", 1, "Uczę się", "7.1A");
            logger.info("Before: {}", student);
            int id = (Integer) session.save(student);
            logger.info("Id: {}", id);
            logger.info("After: {}", student);

            transaction.commit();

        }
    }
}