package br.dev.garage474.msorder.entities;

import br.dev.garage474.msorder.enums.PaymentMethod;
import br.dev.garage474.msorder.enums.PaymentStatus;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Value Object representando as informações de pagamento de um pedido.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @NotNull(message = "Método de pagamento é obrigatório")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @NotNull(message = "Valor total é obrigatório")
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    private String transactionId;

    private LocalDateTime paymentDate;

    private String paymentNotes;

    /**
     * Marca o pagamento como aprovado.
     * @param transactionId ID da transação no gateway de pagamento
     */
    public void approve(String transactionId) {
        this.paymentStatus = PaymentStatus.APPROVED;
        this.transactionId = transactionId;
        this.paymentDate = LocalDateTime.now();
    }

    /**
     * Marca o pagamento como recusado.
     * @param reason motivo da recusa
     */
    public void decline(String reason) {
        this.paymentStatus = PaymentStatus.DECLINED;
        this.paymentNotes = reason;
    }

    /**
     * Marca o pagamento como reembolsado.
     */
    public void refund() {
        this.paymentStatus = PaymentStatus.REFUNDED;
    }
}
