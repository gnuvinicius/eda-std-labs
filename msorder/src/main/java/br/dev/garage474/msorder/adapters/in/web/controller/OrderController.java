package br.dev.garage474.msorder.adapters.in.web.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.dev.garage474.msorder.dto.CreateOrderDto;
import br.dev.garage474.msorder.dto.OrderDto;
import br.dev.garage474.msorder.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller para gerenciamento de pedidos.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * Cria um novo pedido a partir do carrinho.
     * POST /api/v1/orders
     */
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderDto dto) {
        OrderDto orderDto = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
    }

    /**
     * Busca um pedido por ID.
     * GET /api/v1/orders/{orderId}
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable UUID orderId) {
        log.info("Buscando pedido: {}", orderId);
        OrderDto orderDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDto);
    }

    /**
     * Lista pedidos do cliente.
     * GET /api/v1/orders/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<OrderDto>> listCustomerOrders(
        @PathVariable UUID customerId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Listando pedidos do cliente: {}", customerId);
        Page<OrderDto> ordersDto = orderService.listCustomerOrders(customerId, pageable);
        return ResponseEntity.ok(ordersDto);
    }

    /**
     * Lista todos os pedidos.
     * GET /api/v1/orders
     */
    @GetMapping
    public ResponseEntity<Page<OrderDto>> listOrders(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Listando todos os pedidos");
        Page<OrderDto> ordersDto = orderService.listOrders(pageable);
        return ResponseEntity.ok(ordersDto);
    }

    /**
     * Lista pedidos por status.
     * GET /api/v1/orders/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<OrderDto>> listOrdersByStatus(
        @PathVariable String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Listando pedidos com status: {}", status);
        Page<OrderDto> ordersDto = orderService.listOrdersByStatus(status, pageable);
        return ResponseEntity.ok(ordersDto);
    }

    /**
     * Confirma um pedido.
     * PUT /api/v1/orders/{orderId}/confirm
     */
    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<OrderDto> confirmOrder(@PathVariable UUID orderId) {
        log.info("Confirmando pedido: {}", orderId);
        OrderDto orderDto = orderService.confirmOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

    /**
     * Cancela um pedido.
     * PUT /api/v1/orders/{orderId}/cancel
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable UUID orderId) {
        log.info("Cancelando pedido: {}", orderId);
        OrderDto orderDto = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

    /**
     * Finaliza um pedido e envia para processamento assincrono.
     * PUT /api/v1/orders/{orderId}/finalize
     */
    @PutMapping("/{orderId}/finalize")
    public ResponseEntity<OrderDto> finalizeOrder(@PathVariable UUID orderId) {
        log.info("Finalizando pedido: {}", orderId);
        OrderDto orderDto = orderService.finalizeOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }
}

