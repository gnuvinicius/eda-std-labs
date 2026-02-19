package br.dev.garage474.service;

import br.dev.garage474.dto.CreateCustomerDto;
import br.dev.garage474.dto.CustomerDto;
import br.dev.garage474.entity.Customer;
import br.dev.garage474.repository.CustomerRepository;
import jakarta.inject.Inject;
import jakarta.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@WebService(
        serviceName = "CustomerService",
        portName = "CustomerServicePort",
        targetNamespace = "http://service.garage474.dev.br/",
        endpointInterface = "br.dev.garage474.service.CustomerService"
)
public class CustomerServiceImpl implements  CustomerService {

    @Inject
    private CustomerRepository repository;

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public CustomerDto createCustomer(CreateCustomerDto customerDto, UUID tenantId) {
        try {
            Customer customer = customerDto.toEntity(tenantId);
            Customer saved = repository.save(customer);
            return CustomerDto.mapToDto(saved);
        } catch (Exception ex) {
            log.error("Erro ao criar cliente: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
