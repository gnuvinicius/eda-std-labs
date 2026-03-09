package br.dev.garage474.msorder.dtos;

import java.util.List;

import br.dev.garage474.msorder.entities.Order;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "getAllOrdersResponse", namespace = "http://service.garage474.dev.br/")
@XmlType(name = "GetAllOrdersResponse", namespace = "http://service.garage474.dev.br/", propOrder = {
    "orders"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAllOrdersResponse {
    

    @XmlElementWrapper(name = "orders", namespace = "http://service.garage474.dev.br/")
    @XmlElement(name = "order", namespace = "http://service.garage474.dev.br/")
    private List<OrderDto> orders;

    public GetAllOrdersResponse(List<Order> orders) {
        this.orders = orders.stream()
                .map(order -> new OrderDto(order.getId(), order.getCustomerId(), order.getProductId(), order.getQuantity()))
                .toList();
    }

    public List<OrderDto> getOrders() {
        return orders;
    }
}
