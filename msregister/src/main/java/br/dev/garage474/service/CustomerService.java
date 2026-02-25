package br.dev.garage474.service;

import br.dev.garage474.dto.CreateCustomerDto;
import br.dev.garage474.dto.CustomerDto;
import br.dev.garage474.dto.GetAllCustomersResponse;
import br.dev.garage474.entity.Customer;
import br.dev.garage474.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;


@WebService(
        serviceName = "CustomerService",
        portName = "CustomerServicePort",
        targetNamespace = "http://service.garage474.dev.br/"
)
@ApplicationScoped
public class CustomerService {

    @Inject
    private CustomerRepository repository;

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @WebMethod(operationName = "createCustomer")
    public CustomerDto createCustomer(@WebParam(name = "customerDto") CreateCustomerDto customerDto,
                                      @WebParam(name = "tenantId") UUID tenantId) {
        try {
            Customer customer = customerDto.toEntity(tenantId);
            Customer saved = repository.save(customer);
            return CustomerDto.mapToDto(saved);
        } catch (Exception ex) {
            log.error("Erro ao criar cliente: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @WebMethod(operationName = "getCustomerById")
    public CustomerDto getCustomerById(@WebParam(name = "customerId") UUID customerId) {
        return repository.findById(customerId)
                .map(CustomerDto::mapToDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    @WebMethod(operationName = "getAllCustomers")
    public GetAllCustomersResponse getAllCustomers(@WebParam(name = "tenantId") UUID tenantId) {
        log.info("Buscando todos os customers: {}", tenantId);
        try {
            List<CustomerDto> result = repository.findAllByTenantId(tenantId).stream()
                    .map(CustomerDto::mapToDto)
                    .toList();
            return new GetAllCustomersResponse(result);
        } catch (Exception e) {
            log.error("Erro ao buscar todos os clientes: {}", e.getMessage(), e);
            throw e;
        }
    }
}