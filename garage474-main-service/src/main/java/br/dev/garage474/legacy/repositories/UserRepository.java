package br.dev.garage474.legacy.repositories;

import br.dev.garage474.legacy.models.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class UserRepository {

    @PersistenceContext(unitName = "legacyPU")
    private EntityManager em;

    public User findUserById(Long id) {
        return em.find(User.class, id);
    }

    public List<User> findAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
}
