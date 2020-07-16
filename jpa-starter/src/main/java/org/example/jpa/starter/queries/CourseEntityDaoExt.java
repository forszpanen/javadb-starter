package org.example.jpa.starter.queries;

import org.example.jpa.starter.Utils;
import org.example.jpa.starter.queries.entities.CourseEntity;
import org.example.jpa.starter.queries.entities.EntitiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CourseEntityDaoExt {
    private static Logger logger = LoggerFactory.getLogger(CourseEntityDaoExt.class);
    private EntityManagerFactory entityManagerFactory;
    private EntityManager em;

    public CourseEntityDaoExt() {
        entityManagerFactory = Persistence.createEntityManagerFactory("org.example.jpa.starter.queries");
        em = entityManagerFactory.createEntityManager();
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void close() {
        entityManagerFactory.close();
    }

    public static void main(String[] args) {
        CourseEntityDaoExt dao = new CourseEntityDaoExt();
        try {
            EntitiesLoader.fillDataBase(dao.getEntityManagerFactory());
            List<CourseEntity> result = dao.findByCity("Gdynia");
            printList(result);
            result = dao.findByName("Angular", 0, 2);
            printList(result);
            result = dao.findByDateRange(Utils.parse("2018-01-01"), Utils.parse("2018-12-31"));
            printList(result);
            result = dao.findByCities(Set.of("Gdynia", "Sopot"));
            printList(result);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            dao.close();
        }
    }

    private static void printList(List list) {
        logger.info("entityList: \n{}", list.stream()
                .map(element ->
                        {
                            if (element instanceof Object[]) {
                                return Arrays.stream((Object[]) element).map(Object::toString).collect(Collectors.joining(", "));
                            } else {
                                return element.toString();
                            }
                        }
                )
                .collect(Collectors.joining("\n")));
    }

    /**
     * Metoda wyszukuje wszystkie kursy znajdujące się w mieście podanym w argumencie 'city', posortowane malejąco po dacie rozpoczęcia.
     */
    public List<CourseEntity> findByCity(String city) {
        List<CourseEntity> results;
        TypedQuery<CourseEntity> query = em.createQuery("SELECT c FROM CourseEntity c WHERE c.place = :city ORDER BY c.startDate DESC", CourseEntity.class)
                .setParameter("city", city);
        return query.getResultList();
    }

    /**
     * Metoda znajduje wszystkie kursy o nazwie zaczynającej się od frazy 'prefix', przycinając wyniki do ilości określonej parametrem: 'max'
     * i zaczynając od kursu z indeksem: 'from' (użyj: LIKE ‘%fraza%’)
     */
    public List<CourseEntity> findByName(String prefix, int from, int max) {
        TypedQuery<CourseEntity> query = em.createQuery("FROM CourseEntity c WHERE c.name LIKE :code", CourseEntity.class)
                .setParameter("code", prefix + "%")
                .setMaxResults(max)
                .setFirstResult(from);
        return query.getResultList();
    }

    /**
     * Metoda znajduje kursy które zaczynają się w podanym zakresie dat.
     */
    public List<CourseEntity> findByDateRange(Date from, Date to) {
        TypedQuery<CourseEntity> query = em.createQuery("FROM CourseEntity c WHERE c.startDate BETWEEN :from AND :to", CourseEntity.class)
                .setParameter("from", from)
                .setParameter("to", to);
        return query.getResultList();
    }

    /**
     * Metoda znajduje kursy odbywające się w podanych miastach, posortowane po nazwie rosnąco.
     */
    public List<CourseEntity> findByCities(Set<String> cities) {
        TypedQuery<CourseEntity> query = em.createQuery("FROM CourseEntity c WHERE c.place IN :cities", CourseEntity.class)
                .setParameter("cities", cities);
        return query.getResultList();
    }
}