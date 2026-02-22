package br.dev.garage474.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EntityManagerProvider {

    private static final Logger log = LoggerFactory.getLogger(EntityManagerProvider.class);
    private static final String PERSISTENCE_UNIT_NAME = "msregisterPU";
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = createEntityManagerFactory();

    private EntityManagerProvider() {
    }

    private static EntityManagerFactory createEntityManagerFactory() {
        try {
            return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            log.error("Erro ao criar EntityManagerFactory: {}", e.getMessage(), e);
            throw e;
        }
    }

    public static EntityManager getEntityManager() {
        try {
            return ENTITY_MANAGER_FACTORY.createEntityManager();
        } catch (Exception e) {
            log.error("Erro ao criar EntityManager: {}", e.getMessage(), e);
            throw e;
        }
    }
}

