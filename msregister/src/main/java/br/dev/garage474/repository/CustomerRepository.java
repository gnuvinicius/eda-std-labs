package br.dev.garage474.repository;

import br.dev.garage474.entity.Customer;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerRepository {

    @PersistenceContext(unitName = "msregisterPU")
    private EntityManager em;

    private static final Logger log = LoggerFactory.getLogger(CustomerRepository.class);

    public Customer save(Customer customer) {
        try {
            em.persist(customer);
            return customer;
        } catch (Exception e) {
            log.error("Erro ao cadastrar cliente: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Optional<Customer> findById(UUID customerId) {
        try {
            Customer customer = em.find(Customer.class, customerId);
            return customer != null ? Optional.of(customer) : Optional.empty();
        } catch (Exception e) {
            log.error("Erro ao buscar clientes por customerId: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Customer> findAllByTenantId(UUID tenantId) {
        try {
            return em.createQuery("SELECT c FROM Customer c WHERE c.tenantId = :tenantId", Customer.class)
                    .setParameter("tenantId", tenantId)
                    .getResultList();
        } catch (Exception e) {
            log.error("Erro ao buscar clientes por tenantId: {}", e.getMessage(), e);
            throw e;
        }
    }
}
