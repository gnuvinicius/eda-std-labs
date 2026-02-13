package br.dev.garage474.mspedido.services.impl;

import br.dev.garage474.mspedido.dto.CreateOrderDto;
import br.dev.garage474.mspedido.dto.OrderDto;
import br.dev.garage474.mspedido.models.Cart;
import br.dev.garage474.mspedido.models.CartItem;
import br.dev.garage474.mspedido.models.CartStatus;
import br.dev.garage474.mspedido.models.Money;
import br.dev.garage474.mspedido.models.Order;
import br.dev.garage474.mspedido.models.OrderStatus;
import br.dev.garage474.mspedido.repositories.CartRepository;
import br.dev.garage474.mspedido.repositories.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para o serviço de pedidos.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrderServiceImpl Tests")
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID customerId = UUID.randomUUID();
    private final UUID cartId = UUID.randomUUID();
    private final UUID orderId = UUID.randomUUID();

    @Test
    @DisplayName("Deve criar um novo pedido com sucesso")
    void testCreateOrderSuccess() {
        // Arrange
        CreateOrderDto dto = CreateOrderDto.builder()
            .cartId(cartId)
            .customerId(customerId)
            .build();

        CartItem item = new CartItem();
        item.setId(UUID.randomUUID());
        item.setProductId(UUID.randomUUID());
        item.setProductVariantId(UUID.randomUUID());
        item.setQuantity(2);
        item.setUnitPrice(new Money(BigDecimal.valueOf(100.00), "BRL"));
        item.setTenantId(tenantId);

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setCustomerId(customerId);
        cart.setStatus(CartStatus.ACTIVE);
        cart.setTenantId(tenantId);
        cart.setItems(Arrays.asList(item));
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());

        Order order = new Order();
        order.setId(orderId);
        order.setCartId(cartId);
        order.setCustomerId(customerId);
        order.setStatus(OrderStatus.PENDING);
        order.setTotal(new Money(BigDecimal.valueOf(200.00), "BRL"));
        order.setTenantId(tenantId);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        when(cartRepository.findByIdAndTenantId(cartId, tenantId))
            .thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        OrderDto result = orderService.createOrder(tenantId, dto);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals(OrderStatus.PENDING.toString(), result.getStatus());
        verify(orderRepository).save(any(Order.class));
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar pedido com carrinho vazio")
    void testCreateOrderWithEmptyCart() {
        // Arrange
        CreateOrderDto dto = CreateOrderDto.builder()
            .cartId(cartId)
            .customerId(customerId)
            .build();

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setCustomerId(customerId);
        cart.setStatus(CartStatus.ACTIVE);
        cart.setTenantId(tenantId);
        cart.setItems(Arrays.asList());
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());

        when(cartRepository.findByIdAndTenantId(cartId, tenantId))
            .thenReturn(Optional.of(cart));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.createOrder(tenantId, dto));
    }

    @Test
    @DisplayName("Deve buscar um pedido por ID com sucesso")
    void testGetOrderByIdSuccess() {
        // Arrange
        Order order = new Order();
        order.setId(orderId);
        order.setCartId(cartId);
        order.setCustomerId(customerId);
        order.setStatus(OrderStatus.PENDING);
        order.setTotal(new Money(BigDecimal.valueOf(100.00), "BRL"));
        order.setTenantId(tenantId);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        when(orderRepository.findByIdAndTenantId(orderId, tenantId))
            .thenReturn(Optional.of(order));

        // Act
        OrderDto result = orderService.getOrderById(tenantId, orderId);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository).findByIdAndTenantId(orderId, tenantId);
    }

    @Test
    @DisplayName("Deve confirmar um pedido com sucesso")
    void testConfirmOrderSuccess() {
        // Arrange
        Order order = new Order();
        order.setId(orderId);
        order.setCartId(cartId);
        order.setCustomerId(customerId);
        order.setStatus(OrderStatus.PENDING);
        order.setTotal(new Money(BigDecimal.valueOf(100.00), "BRL"));
        order.setTenantId(tenantId);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order confirmedOrder = new Order();
        confirmedOrder.setId(orderId);
        confirmedOrder.setCartId(cartId);
        confirmedOrder.setCustomerId(customerId);
        confirmedOrder.setStatus(OrderStatus.CONFIRMED);
        confirmedOrder.setTotal(new Money(BigDecimal.valueOf(100.00), "BRL"));
        confirmedOrder.setTenantId(tenantId);
        confirmedOrder.setCreatedAt(LocalDateTime.now());
        confirmedOrder.setUpdatedAt(LocalDateTime.now());

        when(orderRepository.findByIdAndTenantId(orderId, tenantId))
            .thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(confirmedOrder);

        // Act
        OrderDto result = orderService.confirmOrder(tenantId, orderId);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.CONFIRMED.toString(), result.getStatus());
        verify(orderRepository).findByIdAndTenantId(orderId, tenantId);
        verify(orderRepository).save(any(Order.class));
    }
}

