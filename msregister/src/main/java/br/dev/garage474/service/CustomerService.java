package br.dev.garage474.service;

import br.dev.garage474.dto.CreateCustomerDto;
import br.dev.garage474.dto.CustomerDto;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

import java.util.UUID;

@WebService(
        name = "CustomerService",
        targetNamespace = "http://service.garage474.dev.br/"
)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface CustomerService {

    @WebMethod(operationName = "createCustomer")
    CustomerDto createCustomer(@WebParam(name = "customerDto") CreateCustomerDto customerDto,
                               @WebParam(name = "tenantId") UUID tenantId);

}
