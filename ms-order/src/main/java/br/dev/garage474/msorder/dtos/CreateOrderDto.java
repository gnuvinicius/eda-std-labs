package br.dev.garage474.msorder.dtos;

import br.dev.garage474.msorder.entities.Order;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "createOrderDto", namespace = "http://service.garage474.dev.br/")
@XmlType(name = "createOrderDto", namespace = "http://service.garage474.dev.br/", propOrder = {
    "customerId", "productId", "quantity"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateOrderDto {
    
    @XmlElement
    private String customerId;
    @XmlElement
    private String productId;
    @XmlElement
    private int quantity;

    public Order toEntity() {
        return new Order(customerId, productId, quantity);
    }
    
}
