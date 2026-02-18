package br.dev.garage474.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class ClientRepository {

    @PersistenceContext(unitName = "msregisterPU")
    private EntityManager em;


}
