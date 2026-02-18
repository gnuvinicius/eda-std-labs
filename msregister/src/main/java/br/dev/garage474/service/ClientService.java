package br.dev.garage474.service;

import br.dev.garage474.repository.ClientRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class ClientService {

    @Inject
    private ClientRepository repository;
}
