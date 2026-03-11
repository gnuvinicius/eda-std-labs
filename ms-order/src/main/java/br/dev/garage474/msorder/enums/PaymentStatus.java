package br.dev.garage474.msorder.enums;

import lombok.Getter;

/**
 * Enum representando os possíveis estados de pagamento de um pedido.
 */
@Getter
public enum PaymentStatus {
    PENDING("Pagamento pendente"),
    PROCESSING("Pagamento em processamento"),
    APPROVED("Pagamento aprovado"),
    DECLINED("Pagamento recusado"),
    REFUNDED("Pagamento reembolsado"),
    CANCELLED("Pagamento cancelado");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

}
