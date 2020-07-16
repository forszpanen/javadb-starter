package org.example.jpa.starter.relations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaRelations {
    private static Logger logger = LoggerFactory.getLogger(JpaRelations.class);
    private EntityManagerFactory entityManagerFactory;

    public JpaRelations() {
        entityManagerFactory = Persistence.createEntityManagerFactory("org.example.jpa.starter.relations");
    }

    public void close() {
        entityManagerFactory.close();
    }

    private void oneToOne() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            AddressEntity address = new AddressEntity("Gdańsk", "Malwinowa 1/3");
            StudentEntity student = new StudentEntity("Jan Kowalski");
            student.setAddress(address);

            entityManager.persist(student);

            StudentEntity studentEntity = entityManager.find(StudentEntity.class, 1);
            logger.info("Student: {}", studentEntity);

            AddressEntity addressEntity = entityManager.find(AddressEntity.class, 1);
            logger.info("AddressEntity Student: {}", addressEntity.getStudent());

            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void oneToMany() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            CourseEntity course = new CourseEntity("JavaGda11", "Sopot");
            StudentEntity student = new StudentEntity("Jan Kowalski");
            course.addStudent(student);

            entityManager.persist(student);

            StudentEntity studentEntity = entityManager.find(StudentEntity.class, 1);
            logger.info("Student: {}", studentEntity);

            CourseEntity courseEntity = entityManager.find(CourseEntity.class, 1);
            logger.info("Course: {}", courseEntity);

            StudentEntity toUpdate = courseEntity.getStudents().stream().findFirst().get();
            toUpdate.setName("John");
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void manyToMany() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            SkillEntity skill1 = new SkillEntity("JVM Master");
            SkillEntity skill2 = new SkillEntity("JDBC Master");
            SkillEntity skill3 = new SkillEntity("Hibernate Master");
            StudentEntity student = new StudentEntity("Jan Kowalski");
            StudentEntity student2 = new StudentEntity("Tomasz Kowalski");
            student.addSkill(skill1);
            student.addSkill(skill2);
            student.addSkill(skill3);
            student2.addSkill(skill1);

            entityManager.persist(student);

            SkillEntity skillToDelete = entityManager.createQuery("FROM SkillEntity WHERE name='JVM Master'",SkillEntity.class).getSingleResult();
            StudentEntity studentEntity = entityManager.find(StudentEntity.class, 1);
            logger.info("Student: {}", studentEntity);
            logger.info("Skill: {}", skillToDelete);
            skillToDelete.getStudents().stream().forEach(s -> s.getSkills().remove(skillToDelete));
            entityManager.remove(skillToDelete);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void newOneToOne() {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            StudentEntity student = new StudentEntity("Jan Kowalski");
            SeatEntity seat = new SeatEntity("A", 1, 1);
            student.setSeat(seat);

            entityManager.persist(student);

            StudentEntity studentEntity = entityManager.find(StudentEntity.class, 1);
            logger.info("Student: {}", studentEntity);

            SeatEntity seatEntity = entityManager.find(SeatEntity.class, 1);
            logger.info("SeatEntity Student: {}", seatEntity.getStudent());

            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    public static void main(String[] args) {
        JpaRelations jpaQueries = new JpaRelations();
        try {
            //jpaQueries.oneToOne();
//            jpaQueries.newOneToOne();
//            jpaQueries.oneToMany();
            jpaQueries.manyToMany();
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            jpaQueries.close();
        }
    }
}