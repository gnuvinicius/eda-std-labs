package br.dev.garage474.mspedido.models;

/**
 * Enum que representa os possíveis status de um pedido.
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}

