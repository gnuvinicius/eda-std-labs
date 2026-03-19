package br.dev.garage474.msorder.dtos;

import br.dev.garage474.msorder.enums.PaymentMethod;
import br.dev.garage474.msorder.enums.PaymentStatus;
import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO representing payment information for an order.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "paymentDto", namespace = "https://service.garage474.dev.br/")
@XmlType(name = "paymentDto", namespace = "https://service.garage474.dev.br/", propOrder = {
        "id", "paymentMethod", "totalAmount", "paymentStatus", "transactionId", "paymentDate", "paymentNotes"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentDto {

    @XmlElement
    private Long id;

    @XmlElement
    private PaymentMethod paymentMethod;

    @XmlElement
    private BigDecimal totalAmount;

    @XmlElement
    private PaymentStatus paymentStatus;

    @XmlElement
    private String transactionId;

    @XmlElement
    private LocalDateTime paymentDate;

    @XmlElement
    private String paymentNotes;
}

