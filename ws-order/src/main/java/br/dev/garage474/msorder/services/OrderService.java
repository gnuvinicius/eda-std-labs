package br.dev.garage474.msorder.services;

import br.dev.garage474.msorder.dtos.CreateOrderDto;
import br.dev.garage474.msorder.dtos.GetAllOrdersResponse;
import br.dev.garage474.msorder.dtos.OrderDto;
import br.dev.garage474.msorder.entities.Order;
import br.dev.garage474.msorder.mappers.OrderMapper;
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

    @Inject
    private OrderMapper orderMapper;

    @WebMethod(operationName = "getAllOrders")
    public GetAllOrdersResponse getAllOrders() {
        log.info("getAllOrders");
        return new GetAllOrdersResponse(orderRepository.findAll());
    }

    /**
     * Creates a new order from the provided DTO.
     *
     * @param createOrderDto DTO containing all order creation data
     * @return {@link OrderDto} with success=true and full order data, or success=false with an error message
     */
    @WebMethod(operationName = "createOrder")
    public OrderDto createOrder(@WebParam(name = "createOrderDto") CreateOrderDto createOrderDto) {
        try {
            log.info("createOrder - customerId={}", createOrderDto.getCustomerId());
            Order order = orderMapper.toEntity(createOrderDto);
            orderRepository.save(order);
            OrderDto response = orderMapper.toDto(order);
            log.info("createOrder - order created successfully, id={}", order.getId());
            return response;
        } catch (Exception e) {
            log.error("createOrder - error creating order: {}", e.getMessage(), e);
            return OrderDto.error(e.getMessage());
        }
    }
}
