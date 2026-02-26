package br.dev.garage474.msorder.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class OrderRepository {

    @PersistenceContext(unitName = "msregisterPU")
    private EntityManager em;
    
    public void save(Object order) {
        em.persist(order);
    }

}
