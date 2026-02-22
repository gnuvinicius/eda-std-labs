package br.dev.garage474.repository;

import br.dev.garage474.config.EntityManagerProvider;
import br.dev.garage474.entity.Customer;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CustomerRepository {

    private static final Logger log = LoggerFactory.getLogger(CustomerRepository.class);

    @Transactional
    public Customer save(Customer customer) {
        EntityManager em = EntityManagerProvider.getEntityManager();
        em.persist(customer);
        return customer;
    }

    public Optional<Customer> findById(UUID customerId) {
        try (EntityManager em = EntityManagerProvider.getEntityManager()) {
            Customer customer = em.find(Customer.class, customerId);
            return customer != null ? Optional.of(customer) : Optional.empty();
        } catch (Exception e) {
            log.error("Erro ao buscar clientes por customerId: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Customer> findAllByTenantId(UUID tenantId) {
        try (EntityManager em = EntityManagerProvider.getEntityManager()) {
            return em.createQuery("SELECT c FROM Customer c WHERE c.tenantId = :tenantId", Customer.class)
                    .setParameter("tenantId", tenantId)
                    .getResultList();
        } catch (Exception e) {
            log.error("Erro ao buscar clientes por tenantId: {}", e.getMessage(), e);
            throw e;
        }
    }
}
