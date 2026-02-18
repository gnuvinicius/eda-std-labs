package br.dev.garage474.msorder.services.impl;

import br.dev.garage474.msorder.config.RabbitMQConfig;
import br.dev.garage474.msorder.dto.CreateOrderDto;
import br.dev.garage474.msorder.dto.OrderDto;
import br.dev.garage474.msorder.dto.OrderItemDto;
import br.dev.garage474.msorder.models.Cart;
import br.dev.garage474.msorder.models.CartStatus;
import br.dev.garage474.msorder.models.Money;
import br.dev.garage474.msorder.models.Order;
import br.dev.garage474.msorder.models.OrderItem;
import br.dev.garage474.msorder.models.OrderStatus;
import br.dev.garage474.msorder.repositories.CartRepository;
import br.dev.garage474.msorder.repositories.OrderRepository;
import br.dev.garage474.msorder.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de gerenciamento de pedidos.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public OrderDto createOrder(UUID tenantId, CreateOrderDto dto) {
        try {
            log.info("Criando novo pedido para tenant: {}, carrinho: {}", tenantId, dto.getCartId());

            // Busca o carrinho
            Cart cart = cartRepository.findByIdAndTenantId(dto.getCartId(), tenantId)
                    .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

            if (cart.getItems().isEmpty()) {
                throw new RuntimeException("Carrinho vazio");
            }

            // Cria o pedido
            Order order = new Order();
            order.setId(UUID.randomUUID());
            order.setCartId(dto.getCartId());
            order.setCustomerId(dto.getCustomerId());
            order.setStatus(OrderStatus.PENDING);
            order.setTenantId(tenantId);

            // Adiciona itens do carrinho ao pedido
            cart.getItems().forEach(cartItem -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setId(UUID.randomUUID());
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setProductVariantId(cartItem.getProductVariantId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setUnitPrice(cartItem.getUnitPrice());
                orderItem.setTenantId(tenantId);
                order.addItem(orderItem);
            });

            // Calcula o total
            order.calculateTotal();

            Order savedOrder = orderRepository.save(order);

            // Atualiza status do carrinho
            cart.setStatus(CartStatus.CONVERTED);
            cartRepository.save(cart);

            log.info("Pedido criado com sucesso: {}", savedOrder.getId());
            return OrderDto.toDto(savedOrder);
        } catch (Exception e) {
            log.error("Erro ao criar pedido: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(UUID tenantId, UUID orderId) {
        try {
            log.info("Buscando pedido: {} para tenant: {}", orderId, tenantId);

            Order order = orderRepository.findByIdAndTenantId(orderId, tenantId)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

            return OrderDto.toDto(order);
        } catch (Exception e) {
            log.error("Erro ao buscar pedido {}: {}", orderId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> listCustomerOrders(UUID tenantId, UUID customerId, Pageable pageable) {
        try {
            log.info("Listando pedidos do cliente: {} para tenant: {}", customerId, tenantId);

            return orderRepository.findByTenantIdAndCustomerId(tenantId, customerId, pageable)
                    .map(OrderDto::toDto);
        } catch (Exception e) {
            log.error("Erro ao listar pedidos do cliente: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> listOrders(UUID tenantId, Pageable pageable) {
        try {
            log.info("Listando todos os pedidos para tenant: {}", tenantId);

            return orderRepository.findByTenantId(tenantId, pageable)
                    .map(OrderDto::toDto);
        } catch (Exception e) {
            log.error("Erro ao listar pedidos: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> listOrdersByStatus(UUID tenantId, String status, Pageable pageable) {
        try {
            log.info("Listando pedidos com status: {} para tenant: {}", status, tenantId);

            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return orderRepository.findByTenantIdAndStatus(tenantId, orderStatus, pageable)
                    .map(OrderDto::toDto);
        } catch (Exception e) {
            log.error("Erro ao listar pedidos por status: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public OrderDto confirmOrder(UUID tenantId, UUID orderId) {
        try {
            log.info("Confirmando pedido: {} para tenant: {}", orderId, tenantId);

            Order order = orderRepository.findByIdAndTenantId(orderId, tenantId)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

            order.setStatus(OrderStatus.CONFIRMED);
            Order updatedOrder = orderRepository.save(order);

            log.info("Pedido confirmado com sucesso: {}", orderId);
            return OrderDto.toDto(updatedOrder);
        } catch (Exception e) {
            log.error("Erro ao confirmar pedido: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(UUID tenantId, UUID orderId) {
        try {
            log.info("Cancelando pedido: {} para tenant: {}", orderId, tenantId);

            Order order = orderRepository.findByIdAndTenantId(orderId, tenantId)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

            order.cancel();
            Order updatedOrder = orderRepository.save(order);

            log.info("Pedido cancelado com sucesso: {}", orderId);
            return OrderDto.toDto(updatedOrder);
        } catch (Exception e) {
            log.error("Erro ao cancelar pedido: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void sendOrderToQueue(UUID tenantId, UUID orderId) {
        try {
            log.info("Enviando pedido para fila: {} para tenant: {}", orderId, tenantId);

            Order order = orderRepository.findByIdAndTenantId(orderId, tenantId)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

            // Cria a mensagem de pedido
            Map<String, Object> orderMessage = new HashMap<>();
            orderMessage.put("orderId", order.getId().toString());
            orderMessage.put("customerId", order.getCustomerId().toString());
            orderMessage.put("tenantId", tenantId.toString());
            orderMessage.put("status", order.getStatus().toString());
            orderMessage.put("totalAmount", order.getTotal().getAmount());
            orderMessage.put("totalCurrency", order.getTotal().getCurrency());
            orderMessage.put("items", order.getItems().stream()
                    .map(item -> {
                        Map<String, Object> itemMap = new HashMap<>();
                        itemMap.put("productId", item.getProductId().toString());
                        itemMap.put("productVariantId", item.getProductVariantId().toString());
                        itemMap.put("quantity", item.getQuantity());
                        itemMap.put("unitPrice", item.getUnitPrice().getAmount());
                        return itemMap;
                    })
                    .collect(Collectors.toList()));

            // Envia para a fila
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_ROUTING_KEY,
                    objectMapper.writeValueAsString(orderMessage)
            );

            log.info("Pedido enviado para fila com sucesso: {}", orderId);
        } catch (Exception e) {
            log.error("Erro ao enviar pedido para fila: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao enviar pedido para fila", e);
        }
    }

    @Override
    @Transactional
    public OrderDto finalizeOrder(UUID tenantId, UUID orderId) {
        try {
            log.info("Finalizando pedido: {} para tenant: {}", orderId, tenantId);

            Order order = orderRepository.findByIdAndTenantId(orderId, tenantId)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

            // Atualiza status
            order.setStatus(OrderStatus.PROCESSING);
            Order updatedOrder = orderRepository.save(order);

            // Envia para a fila
            sendOrderToQueue(tenantId, orderId);

            log.info("Pedido finalizado e enviado para fila com sucesso: {}", orderId);
            return OrderDto.toDto(updatedOrder);
        } catch (Exception e) {
            log.error("Erro ao finalizar pedido: {}", e.getMessage(), e);
            throw e;
        }
    }

}

