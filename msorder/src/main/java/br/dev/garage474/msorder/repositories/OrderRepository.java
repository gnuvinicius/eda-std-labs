package br.dev.garage474.msorder.repositories;

import java.util.List;

import br.dev.garage474.msorder.entities.Order;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class OrderRepository {

    @PersistenceContext(unitName = "msregisterPU")
    private EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public List<Order> findAll() {
        return em.createQuery("SELECT o FROM Order o", Order.class)
                .getResultList();
    }

}
