package br.dev.garage474.msorder.dtos;

import br.dev.garage474.msorder.entities.Order;
import br.dev.garage474.msorder.mappers.OrderMapper;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "getAllOrdersResponse", namespace = "https://service.garage474.dev.br/")
@XmlType(name = "GetAllOrdersResponse", namespace = "https://service.garage474.dev.br/", propOrder = {
    "orders"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAllOrdersResponse {
    

    @XmlElementWrapper(name = "orders", namespace = "https://service.garage474.dev.br/")
    @XmlElement(name = "order", namespace = "https://service.garage474.dev.br/")
    private List<OrderDto> orders;

    public GetAllOrdersResponse(List<Order> orders) {
        OrderMapper mapper = new OrderMapper();
        this.orders = orders.stream()
                .map(mapper::toDto)
                .toList();
    }

}
