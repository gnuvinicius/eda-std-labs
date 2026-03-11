package br.dev.garage474.msorder.enums;

import lombok.Getter;

/**
 * Enum representando os possíveis estados de um pedido no sistema.
 */
@Getter
public enum OrderStatus {
    PENDING("Pedido pendente de processamento"),
    CONFIRMED("Pedido confirmado"),
    PROCESSING("Pedido em processamento"),
    SHIPPED("Pedido enviado"),
    DELIVERED("Pedido entregue"),
    CANCELLED("Pedido cancelado"),
    FAILED("Pedido com falha no processamento");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

}
