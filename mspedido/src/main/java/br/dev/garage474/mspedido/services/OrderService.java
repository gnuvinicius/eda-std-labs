package br.dev.garage474.mspedido.services;

import br.dev.garage474.mspedido.dto.CreateOrderDto;
import br.dev.garage474.mspedido.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Interface de serviço para gerenciamento de pedidos.
 */
public interface OrderService {

    /**
     * Cria um novo pedido a partir do carrinho.
     */
    OrderDto createOrder(UUID tenantId, CreateOrderDto dto);

    /**
     * Busca um pedido por ID.
     */
    OrderDto getOrderById(UUID tenantId, UUID orderId);

    /**
     * Lista os pedidos de um cliente.
     */
    Page<OrderDto> listCustomerOrders(UUID tenantId, UUID customerId, Pageable pageable);

    /**
     * Lista todos os pedidos de um tenant.
     */
    Page<OrderDto> listOrders(UUID tenantId, Pageable pageable);

    /**
     * Lista pedidos por status.
     */
    Page<OrderDto> listOrdersByStatus(UUID tenantId, String status, Pageable pageable);

    /**
     * Confirma um pedido.
     */
    OrderDto confirmOrder(UUID tenantId, UUID orderId);

    /**
     * Cancela um pedido.
     */
    OrderDto cancelOrder(UUID tenantId, UUID orderId);

    /**
     * Envia um pedido para a fila de processamento.
     */
    void sendOrderToQueue(UUID tenantId, UUID orderId);

    /**
     * Finaliza um pedido e envia para a fila.
     */
    OrderDto finalizeOrder(UUID tenantId, UUID orderId);
}

