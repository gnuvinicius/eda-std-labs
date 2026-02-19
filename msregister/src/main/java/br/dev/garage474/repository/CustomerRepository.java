package br.dev.garage474.repository;

import br.dev.garage474.entity.Customer;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class CustomerRepository {

    @PersistenceContext(unitName = "msregisterPU")
    private EntityManager em;


    public Customer save(Customer customer) {
        try {
            em.persist(customer);
            return customer;
        } catch (Exception ex) {
            // Lidar com a exceção, por exemplo, logando o erro
            System.err.println("Erro ao salvar o cliente: " + ex.getMessage());
            // Você pode lançar uma exceção personalizada ou lidar de outra forma
            throw ex;
        }
    }
}
