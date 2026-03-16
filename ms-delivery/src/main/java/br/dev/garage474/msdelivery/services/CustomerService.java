package br.dev.garage474.msdelivery.services;

import br.dev.garage474.msdelivery.dtos.CreateCustomerRequest;
import br.dev.garage474.msdelivery.dtos.CustomerResponse;
import br.dev.garage474.msdelivery.exceptions.CustomerAlreadyExistsException;
import br.dev.garage474.msdelivery.models.Customer;
import br.dev.garage474.msdelivery.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service responsible for customer registration and retrieval business logic.
 */
@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    /**
     * Registers a new customer after verifying uniqueness of email and document.
     *
     * @param request the creation request DTO
     * @return the created customer as a {@link CustomerResponse}
     * @throws CustomerAlreadyExistsException if email or document is already registered
     */
    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        try {
            log.info("creating customer with email={}", request.email());

            if (customerRepository.existsByEmail(request.email().toLowerCase().trim())) {
                throw new CustomerAlreadyExistsException("email", request.email());
            }

            String normalizedDocument = request.document().replaceAll("[^0-9]", "");
            if (customerRepository.existsByDocument(normalizedDocument)) {
                throw new CustomerAlreadyExistsException("document", request.document());
            }

            Customer customer = customerMapper.toEntity(request);
            Customer savedCustomer = customerRepository.save(customer);

            log.info("customer created with id={} email={}", savedCustomer.getId(), savedCustomer.getEmail());
            return customerMapper.toResponse(savedCustomer);
        } catch (Exception e) {
            log.error("error creating customer with email={}: {}", request.email(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Returns all registered customers.
     *
     * @return list of {@link CustomerResponse}
     */
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAllCustomers() {
        try {
            log.info("listing all customers");
            return customerRepository.findAll()
                    .stream()
                    .map(customerMapper::toResponse)
                    .toList();
        } catch (Exception e) {
            log.error("error listing customers: {}", e.getMessage(), e);
            throw e;
        }
    }
}

