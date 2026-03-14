package br.dev.garage474.msorder.dtos;

import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO representing a single item inside an order.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "orderItemDto", namespace = "https://service.garage474.dev.br/")
@XmlType(name = "orderItemDto", namespace = "https://service.garage474.dev.br/", propOrder = {
        "id", "productId", "productName", "quantity", "unitPrice", "discount"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderItemDto {

    @XmlElement
    private Long id;

    @XmlElement
    private UUID productId;

    @XmlElement
    private String productName;

    @XmlElement
    private Integer quantity;

    @XmlElement
    private BigDecimal unitPrice;

    @XmlElement
    private BigDecimal discount;
}

