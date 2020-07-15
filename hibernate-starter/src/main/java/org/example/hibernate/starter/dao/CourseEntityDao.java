package org.example.hibernate.starter.dao;

import org.example.hibernate.starter.Utils;
import org.example.hibernate.starter.entity.CourseEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CourseEntityDao {
    private static final Logger logger = LoggerFactory.getLogger(CourseEntityDao.class);

    private final SessionFactory sessionFactory;

    public static void main(String[] args) {
        CourseEntityDao dao = new CourseEntityDao();
        CourseEntity entity = new CourseEntity("JavaPoz18", "Poznań", Utils.parse("2019-06-01"), Utils.parse("2019-12-01"));
        Integer id = dao.create(entity);
        entity = dao.read(id);
        logger.info(entity.toString());
        List<CourseEntity> entities = dao.readAll();
        entities.forEach(courseEntity -> logger.info(courseEntity.toString()));
        entity.setName("JavaPoz19");
        dao.update(entity);
        dao.delete(entity.getId());
    }

    public CourseEntityDao() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate-na.cfg.xml")
                .build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(CourseEntity.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Integer create(CourseEntity toCreate) {
        Session session = null;
        Integer id = null;
        try {
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            id = (Integer) session.save(toCreate);
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return id;
    }

    public CourseEntity read(Integer id) {
        Session session = null;
        CourseEntity entity = null;
        try {
            session = sessionFactory.openSession();
            Query<CourseEntity> query = session.createQuery("FROM CourseEntity WHERE id = " + id);
            entity = query.getSingleResult();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return entity;
    }

    public List<CourseEntity> readAll() {
        Session session = null;
        List<CourseEntity> entities = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            Query<CourseEntity> query = session.createQuery("FROM CourseEntity");
            entities = query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return entities;
    }

    public void update(CourseEntity toUpdate) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            CourseEntity updated = session.get(CourseEntity.class, toUpdate.getId());
            session.evict(updated);
            session.saveOrUpdate(toUpdate);
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void delete(Integer id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            CourseEntity toRemove = session.get(CourseEntity.class, id);
            session.delete(toRemove);
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
