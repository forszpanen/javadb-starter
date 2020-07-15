package org.example.hibernate.starter.configuration;

import org.example.hibernate.starter.Utils;
import org.example.hibernate.starter.entity.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateConfiguration {
    private static Logger logger = LoggerFactory.getLogger(HibernateConfiguration.class);

    public static void main(String[] args) {
        /**
         * Krok 1: prosta konfiguracja Hibernate: tworzymy obiekt klasy Configuration i
         * podajemy mu plik z konfiguracją: "hibernate.cfg.xml" - plik znajduje się w katalogu resources
         */
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        /**
         * Krok 2: tworzymy dwa obiekty: SessionFactory i Session z konfiguracji, którą wcześniej przygotowaliśmy
         */
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            /**
             * Krok 3: zaczynamy nową transakcję, każda operacja na bazie danych musi być "otoczona" transakcją
             */
            Transaction transaction = session.beginTransaction();

            Course course = new Course("JavaPoz22", "Poznań", Utils.parse("2020-01-01"), Utils.parse("2020-11-01"));
            logger.info("Before: {}", course);
            Integer id = (Integer) session.save(course);
            logger.info("Id: {}", id);
            logger.info("After: {}", course);

            course = new Course("JavaPoz23", "Poznań", Utils.parse("2020-03-01"), Utils.parse("2021-01-01"));
            logger.info("Before: {}", course);
            id = (Integer) session.save(course);
            logger.info("Id: {}", id);
            logger.info("After: {}", course);

            /**
             * Krok 4: kończymy transakcję - wszystkie dane powinny być zapisane w bazie
             */
            transaction.commit();

            /**
             * Krok 5: niejawnie zamykamy: sessionFactory i session
             */
        }
    }
}