package br.dev.garage474.msdelivery.resources.customer;

import br.dev.garage474.msdelivery.resources.dtos.CreateCustomerRequest;
import br.dev.garage474.msdelivery.resources.dtos.CustomerResponse;
import br.dev.garage474.msdelivery.exceptions.CustomerAlreadyExistsException;
import br.dev.garage474.msdelivery.models.Customer;
import br.dev.garage474.msdelivery.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerResource {

    private final static Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private final CustomerRepository customerRepository;

    public CustomerResource(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CreateCustomerRequest request) {
        try {
            log.info("creating customer with email={}", request.getEmail());

            if (customerRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
                throw new CustomerAlreadyExistsException("email", request.getEmail());
            }

            String normalizedDocument = request.getDocument().replaceAll("[^0-9]", "");
            if (customerRepository.existsByDocument(normalizedDocument)) {
                throw new CustomerAlreadyExistsException("document", request.getDocument());
            }

            Customer customer = request.toEntity();
            Customer savedCustomer = customerRepository.save(customer);

            log.info("customer created with id={} email={}", savedCustomer.getId(), savedCustomer.getEmail());
            return ResponseEntity.ok(CustomerResponse.toResponse(savedCustomer));
        } catch (Exception e) {
            log.error("error creating customer with email={}: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    public ResponseEntity<List<CustomerResponse>> findAllCustomers() {
        try {
            log.info("listing all customers");
            List<CustomerResponse> result = customerRepository.findAll()
                    .stream()
                    .map(CustomerResponse::toResponse)
                    .toList();

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("error listing customers: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
