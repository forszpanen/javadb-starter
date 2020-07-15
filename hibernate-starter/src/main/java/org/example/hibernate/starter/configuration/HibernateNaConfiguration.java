package org.example.hibernate.starter.configuration;

import org.example.hibernate.starter.Utils;
import org.example.hibernate.starter.entity.CourseEntity;
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
                    .buildMetadata()
                    .buildSessionFactory();
            Session session = sessionFactory.openSession();

            CourseEntity course = new CourseEntity("JavaPoz24", "Poznań", Utils.parse("2020-06-01"), Utils.parse("2021-04-01"));
            logger.info("Before: {}", course);

            Transaction transaction = session.beginTransaction();
            Integer id = (Integer) session.save(course);
            transaction.commit();
            session.close();

            logger.info("Id: {}", id);
            logger.info("After: {}", course);

        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
