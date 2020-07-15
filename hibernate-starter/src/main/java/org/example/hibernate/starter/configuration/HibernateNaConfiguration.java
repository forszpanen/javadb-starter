package org.example.hibernate.starter.configuration;

import org.example.hibernate.starter.Utils;
import org.example.hibernate.starter.entity.CourseEntity;
import org.example.hibernate.starter.entity.StudentEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateNaConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(HibernateNaConfiguration.class);

    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate-na.cfg.xml")
                .build();
        try {
            SessionFactory sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(CourseEntity.class)
                    .addAnnotatedClass(StudentEntity.class)
                    .buildMetadata()
                    .buildSessionFactory();
            Session session = sessionFactory.openSession();

            StudentEntity student = new StudentEntity("Jan Kowalski", 1, "Jestem nowy", "5.3.B");
            logger.info("Before: {}", student);

            Transaction transaction = session.beginTransaction();
            Integer id = (Integer) session.save(student);
            transaction.commit();
            session.close();

            logger.info("Id: {}", id);
            logger.info("After: {}", student);

        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
