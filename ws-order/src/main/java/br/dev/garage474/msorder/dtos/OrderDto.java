package br.dev.garage474.msorder.dtos;

import br.dev.garage474.msorder.enums.OrderStatus;
import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO representing the full state of an order, used as the response for order operations.
 * Includes success/error flags for service-level feedback.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "orderDto", namespace = "https://service.garage474.dev.br/")
@XmlType(name = "orderDto", namespace = "https://service.garage474.dev.br/", propOrder = {
        "success", "errorMessage",
        "id", "customerId", "status",
        "items", "shippingAddress", "payment",
        "customerNotes", "shippingCost", "totalDiscount",
        "subtotal", "total",
        "createdAt", "updatedAt", "deliveredAt",
        "cancellationReason"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDto {

    @XmlElement
    private boolean success;

    @XmlElement
    private String errorMessage;

    @XmlElement
    private UUID id;

    @XmlElement
    private UUID customerId;

    @XmlElement
    private OrderStatus status;

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

    @XmlElement
    private BigDecimal subtotal;

    @XmlElement
    private BigDecimal total;

    @XmlElement
    private LocalDateTime createdAt;

    @XmlElement
    private LocalDateTime updatedAt;

    @XmlElement
    private LocalDateTime deliveredAt;

    @XmlElement
    private String cancellationReason;

    /**
     * Factory method for an error response.
     *
     * @param errorMessage the error description
     * @return OrderDto with success=false and the provided error message
     */
    public static OrderDto error(String errorMessage) {
        return OrderDto.builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }
}
