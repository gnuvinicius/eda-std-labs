package br.dev.garage474.msorder.services;

import br.dev.garage474.msorder.dto.CreateOrderDto;
import br.dev.garage474.msorder.dto.OrderDto;
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
    OrderDto createOrder(CreateOrderDto dto);

    /**
     * Busca um pedido por ID.
     */
    OrderDto getOrderById(UUID orderId);

    /**
     * Lista os pedidos de um cliente.
     */
    Page<OrderDto> listCustomerOrders(UUID customerId, Pageable pageable);

    /**
     * Lista todos os pedidos.
     */
    Page<OrderDto> listOrders(Pageable pageable);

    /**
     * Lista pedidos por status.
     */
    Page<OrderDto> listOrdersByStatus(String status, Pageable pageable);

    /**
     * Confirma um pedido.
     */
    OrderDto confirmOrder(UUID orderId);

    /**
     * Cancela um pedido.
     */
    OrderDto cancelOrder(UUID orderId);

    /**
     * Envia um pedido para a fila de processamento.
     */
    void sendOrderToQueue(UUID orderId);

    /**
     * Finaliza um pedido e envia para a fila.
     */
    OrderDto finalizeOrder(UUID orderId);
}

