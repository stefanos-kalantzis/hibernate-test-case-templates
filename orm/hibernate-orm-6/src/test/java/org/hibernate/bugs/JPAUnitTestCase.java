package org.hibernate.bugs;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.MariaDBContainer;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

    private MariaDBContainer mariaDBContainer = new MariaDBContainer<>("mariadb:10.5.21")
            .withUsername("mariadb1")
            .withPassword("mariadb1")
            .withDatabaseName("db1")
            .withInitScript("init-script.sql");

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void init() {
        mariaDBContainer.setPortBindings(List.of("3316:3306"));
        mariaDBContainer.start();
        entityManagerFactory = Persistence.createEntityManagerFactory("templatePU");
    }

    @After
    public void destroy() {
        entityManagerFactory.close();
        mariaDBContainer.stop();
    }

    @Test
    public void hhh17180Test() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.persist(new Animal(AnimalType.CAT, "Fluffies"));

        entityManager.getTransaction().commit();
        entityManager.close();
    }


    @Entity(name = "animal")
    public static class Animal {

        @Id
        @GeneratedValue
        private Integer id;

        // @JdbcTypeCode(SqlTypes.VARCHAR)
        @Enumerated(EnumType.STRING)
        private AnimalType animalType;

        private String name;

        public Animal() {
        }

        public Animal(AnimalType animalType, String name) {
            this.animalType = animalType;
            this.name = name;
        }
    }

    public enum AnimalType {
        CAT, DOG
    }
}
