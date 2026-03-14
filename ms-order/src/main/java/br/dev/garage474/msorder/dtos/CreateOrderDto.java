package br.dev.garage474.msorder.dtos;

import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO for creating a new order, containing all required order data.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "createOrderDto", namespace = "https://service.garage474.dev.br/")
@XmlType(name = "createOrderDto", namespace = "https://service.garage474.dev.br/", propOrder = {
        "customerId", "items", "shippingAddress", "payment", "customerNotes", "shippingCost", "totalDiscount"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateOrderDto {

    @XmlElement
    private UUID customerId;

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<OrderItemDto> items;

    @XmlElement
    private AddressDto shippingAddress;

    @XmlElement
    private PaymentDto payment;

    @XmlElement
    private String customerNotes;

    @XmlElement
    private BigDecimal shippingCost;

    @XmlElement
    private BigDecimal totalDiscount;
}
