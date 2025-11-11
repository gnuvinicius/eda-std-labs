package br.dev.garage474.legacy.repositories;

import java.util.List;

import br.dev.garage474.legacy.models.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@RequestScoped
public class UserRepository {

    @PersistenceContext(unitName = "legacyPU")
    private EntityManager em;

    public User findUserById(Long id) {
        return em.find(User.class, id);
    }

    public List<User> findAllUsers(Integer page, Integer perPage) {
        if (page == null || perPage == null) {
            throw new IllegalArgumentException("page and perPage are required");
        }

        String sql = "SELECT u FROM User u ORDER BY updatedAt DESC";
        TypedQuery<User> query = em.createQuery(sql, User.class);

        int offset = (page - 1) * perPage;
        query.setFirstResult(offset);
        query.setMaxResults(perPage);

        return query.getResultList();
    }

    public int countAllUsers() {
        String sql = "SELECT COUNT(u) FROM User u";
        Long count = em.createQuery(sql, Long.class).getSingleResult();
        return count.intValue();
    }
}
