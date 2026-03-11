package br.dev.garage474.msorder.services;

import br.dev.garage474.msorder.dtos.CreateOrderDto;
import br.dev.garage474.msorder.dtos.GetAllOrdersResponse;
import br.dev.garage474.msorder.repositories.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebService(
        serviceName = "OrderService",
        portName = "OrderServicePort",
        targetNamespace = "https://service.garage474.dev.br/"
)
@ApplicationScoped
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Inject
    private OrderRepository orderRepository;

    @WebMethod(operationName = "createOrder")
    public void createOrder(@WebParam(name = "orderDto") CreateOrderDto orderDto) {
        orderRepository.save(orderDto.toEntity());
    }

    @WebMethod(operationName = "getAllOrders")
    public GetAllOrdersResponse getAllOrders() {
        log.info("getAllOrders");
        return new GetAllOrdersResponse(orderRepository.findAll());
    }
    
}
