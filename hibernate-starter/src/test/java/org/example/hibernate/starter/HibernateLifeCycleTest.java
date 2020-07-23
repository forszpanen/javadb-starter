package org.example.hibernate.starter;

import org.example.hibernate.starter.entity.CourseEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HibernateLifeCycleTest {
    private final SessionFactory sessionFactory;
    private Session session;

    public HibernateLifeCycleTest() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate-test.cfg.xml")
                .build();
        sessionFactory = new MetadataSources(registry)
                .addAnnotatedClass(CourseEntity.class)
                .buildMetadata()
                .buildSessionFactory();
    }

    @Before
    public void setUp() {
        session = sessionFactory.openSession();
    }

    @After
    public void tearDown() {
        session.close();
    }

    @Test
    public void whenTransientEntitySavedThenReturnPersistentEntity() {
        Transaction transaction = session.beginTransaction();
        CourseEntity course = new CourseEntity("JavaPoz25", "Poznań", Utils.parse("2020-04-11"), Utils.parse("2020-11-16"));
        Integer id = (Integer) session.save(course);
        // course w stanie persistent
        Assert.assertNotNull(id);

        //dlatego zmiany w obiekcie są odzwierciedlone w rekordzie
        course.setName("JavaGda26");
        transaction.commit();
        Query<CourseEntity> query = session.createQuery("FROM CourseEntity");
        Assert.assertTrue(query.list().stream().anyMatch(courseEntity -> "JavaGda26".equals(courseEntity.getName())));
    }

    @Test
    public void whenTransientEntitySavedOrUpdatedThenReturnPersistentEntity() {
        Transaction transaction = session.beginTransaction();
        CourseEntity course = new CourseEntity("JavaWro12", "Wrocław", Utils.parse("2020-04-11"), Utils.parse("2020-11-16"));

        //encja w stanie transient - stan obiektu nie jest zarządzany przez hibernate
        Assert.assertNull(course.getId());
        session.saveOrUpdate(course);

        //encja w stanie persistent - obiekt jest zarządzany, ale do wywołania transaction.commit nie ma go w bazie
        Assert.assertNotNull(course.getId());
        transaction.commit();
    }

    @Test
    public void whenPersistentEntityEvictedThenReturnDetachedEntity() {
        Transaction transaction = session.beginTransaction();
        CourseEntity course = new CourseEntity("JavaWar31", "Warszawa", Utils.parse("2020-04-11"), Utils.parse("2020-11-16"));
        session.save(course);

        // persistent -> detached
        session.evict(course);
        transaction.commit();
        Query<CourseEntity> query = session.createQuery("FROM CourseEntity");
        Assert.assertFalse(query.list().stream().anyMatch(courseEntity -> "JavaWar31".equals(courseEntity.getName())));
    }

    @Test
    public void whenEntityDetachedThenChangesNotSavedToDatabase() {
        Transaction transaction = session.beginTransaction();
        CourseEntity course = new CourseEntity("JavaWar27", "Warszawa", Utils.parse("2020-04-11"), Utils.parse("2020-11-16"));
        session.save(course);
        transaction.commit();
        //rozpoczynamy drugą transakcję, po tym, jak dane zostały zapisane do bazy
        transaction = session.beginTransaction();
        session.evict(course);
        course.setEndDate(Utils.parse("2022-01-01"));
        transaction.commit();
        Query<CourseEntity> query = session.createQuery("FROM CourseEntity");
        Assert.assertFalse(query.list().stream().anyMatch(courseEntity -> Utils.parse("2022-01-01").equals(courseEntity.getEndDate())));
    }

    @Test
    public void whenEntityPersistentThenChangesSavedToDatabase() {
        Transaction transaction = session.beginTransaction();
        CourseEntity course = new CourseEntity("JavaWro12", "Wrocław", Utils.parse("2020-04-11"), Utils.parse("2020-11-16"));
        session.save(course);
        transaction.commit();
        transaction = session.beginTransaction();
        course.setEndDate(Utils.parse("2022-04-01"));
        transaction.commit();
        Query<CourseEntity> query = session.createQuery("FROM CourseEntity");
        Assert.assertTrue(query.list().stream().anyMatch(courseEntity -> Utils.parse("2022-04-01").equals(courseEntity.getEndDate())));
    }

    @Test
    public void whenEntityDetachedThenUpdateReturnPersistentEntity() {
        Transaction transaction = session.beginTransaction();
        CourseEntity course = new CourseEntity("JavaPoz6", "Poznań", Utils.parse("2020-04-11"), Utils.parse("2020-11-16"));
        session.save(course);

        //usuwamy obiekt course z kontekstu
        session.evict(course);
        course.setName("JavaPoz7");
        Query<CourseEntity> query = session.createQuery("FROM CourseEntity WHERE name = 'JavaPoz7'");
//        Assert.assertTrue(query.list().isEmpty());

        //dodajemy obiekt z powrotem do kontekstu
        session.update(course);
        Assert.assertFalse(query.list().isEmpty());
        transaction.commit();

    }
}
