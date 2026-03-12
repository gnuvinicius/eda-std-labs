package br.dev.garage474.msorder.entities;

import br.dev.garage474.msorder.enums.PaymentMethod;
import br.dev.garage474.msorder.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Value Object representando as informações de pagamento de um pedido.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "payment_seq", sequenceName = "payment_seq", initialValue = 1, allocationSize = 1)
    private Long id;

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
