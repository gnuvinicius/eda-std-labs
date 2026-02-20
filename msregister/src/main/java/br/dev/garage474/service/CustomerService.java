package br.dev.garage474.service;

import br.dev.garage474.dto.CreateCustomerDto;
import br.dev.garage474.dto.CustomerDto;
import br.dev.garage474.entity.Customer;
import br.dev.garage474.repository.CustomerRepository;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@WebService(
        serviceName = "CustomerService",
        portName = "CustomerServicePort",
        targetNamespace = "http://service.garage474.dev.br/",
        endpointInterface = "br.dev.garage474.service.CustomerService"
)
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
        try {
            Customer customer = repository.findById(customerId);
            if (customer != null) {
                return CustomerDto.mapToDto(customer);
            } else {
                log.warn("Cliente com ID {} não encontrado", customerId);
                return null; // Ou lançar uma exceção personalizada
            }
        } catch (Exception ex) {
            log.error("Erro ao buscar cliente por ID: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @WebMethod(operationName = "getAllCustomers")
    public List<CustomerDto> getAllCustomers(@WebParam(name = "tenantId") UUID tenantId) {
        try {
            List<Customer> customers = repository.findAllByTenantId(tenantId);
            return customers.stream()
                    .map(CustomerDto::mapToDto)
                    .toList();
        } catch (Exception ex) {
            log.error("Erro ao buscar todos os clientes: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
