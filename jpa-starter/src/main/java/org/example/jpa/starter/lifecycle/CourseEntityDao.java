package org.example.jpa.starter.lifecycle;

import org.example.jpa.starter.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class CourseEntityDao {
    private static Logger logger = LoggerFactory.getLogger(CourseEntityDao.class);
    private EntityManagerFactory entityManagerFactory;

    public CourseEntityDao() {
        entityManagerFactory = Persistence.createEntityManagerFactory("org.example.jpa.starter.lifecycle");
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void close() {
        entityManagerFactory.close();
    }

    public void save(CourseEntity courseEntity) {
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            logger.info("Course to persist: {}", courseEntity.toString());
            entityManager.persist(courseEntity);
            logger.info("Course after persist: {}", courseEntity.toString());
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(CourseEntity courseEntity) {
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(courseEntity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(CourseEntity courseEntity) {
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            logger.info("Removing course with id: {}", courseEntity.getId());
            if (entityManager.contains(courseEntity)) {
                entityManager.remove(courseEntity);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CourseEntity findById(int id) {
        CourseEntity entity = null;
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            logger.info("Finding course by id: {}", id);
            entity = entityManager.find(CourseEntity.class, id);
            if (entity != null) {
                logger.info("Found course: {}", entity.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    public List<CourseEntity> list() {
        List<CourseEntity> entities = new ArrayList<>();
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            logger.info("Finding all courses");
            entities = entityManager.createQuery("FROM CourseEntity", CourseEntity.class).getResultList();
            logger.info("Found {} courses", entities.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }

    public static void main(String[] args) {
        CourseEntityDao dao = new CourseEntityDao();
        CourseEntity entity = new CourseEntity("JavaGda11", "Sopot", Utils.parse("2018-01-01"), Utils.parse("2018-09-01"));
        dao.save(entity);
        entity.setName("JavaGda12");
        dao.update(entity);
        CourseEntity found = dao.findById(entity.getId());
        List<CourseEntity> entities = dao.list();
        entities.forEach(courseEntity -> logger.info(courseEntity.toString()));
        dao.delete(entity);
        CourseEntity deleted = dao.findById(entity.getId());
        if (deleted == null) {
            logger.info("Delete successfull");
        }
        CourseEntity toDelete = new CourseEntity("JavaGda9", "Sopot", Utils.parse("2018-01-01"), Utils.parse("2018-09-01"));
        dao.delete(toDelete);
    }

}