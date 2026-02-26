package br.dev.garage474.msorder.services;

import br.dev.garage474.msorder.dtos.CreateOrderDto;
import br.dev.garage474.msorder.repositories.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(
        serviceName = "OrderService",
        portName = "OrderServicePort",
        targetNamespace = "http://service.garage474.dev.br/"
)
@ApplicationScoped
public class OrderService {

    @Inject
    private OrderRepository orderRepository;

    @WebMethod(operationName = "createOrder")
    public void createOrder(@WebParam(name = "orderDto") CreateOrderDto orderDto) {
        orderRepository.save(orderDto);
    }
    
}
