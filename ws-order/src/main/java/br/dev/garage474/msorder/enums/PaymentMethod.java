package br.dev.garage474.msorder.enums;

import lombok.Getter;

/**
 * Enum representando os métodos de pagamento disponíveis.
 */
@Getter
public enum PaymentMethod {

    CREDIT_CARD("Cartão de Crédito"),
    DEBIT_CARD("Cartão de Débito"),
    PIX("PIX"),
    BOLETO("Boleto Bancário"),
    PAYPAL("PayPal"),
    BANK_TRANSFER("Transferência Bancária");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

}
