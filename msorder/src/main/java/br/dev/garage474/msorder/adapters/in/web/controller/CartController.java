package br.dev.garage474.msorder.adapters.in.web.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.dev.garage474.msorder.dto.CartDto;
import br.dev.garage474.msorder.dto.CreateCartDto;
import br.dev.garage474.msorder.dto.CreateCartItemDto;
import br.dev.garage474.msorder.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller para gerenciamento de carrinhos de compras.
 */
@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    /**
     * Cria um novo carrinho.
     * POST /api/v1/carts
     */
    @PostMapping
    public ResponseEntity<CartDto> createCart(@Valid @RequestBody CreateCartDto dto) {
        CartDto cartDto = cartService.createCart(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }

    /**
     * Busca um carrinho por ID.
     * GET /api/v1/carts/{cartId}
     */
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        CartDto cartDto = cartService.getCartById(cartId);
        return ResponseEntity.ok(cartDto);
    }

    /**
     * Lista carrinhos do cliente.
     * GET /api/v1/carts/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<CartDto>> listCustomerCarts(
        @PathVariable UUID customerId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Listando carrinhos do cliente: {}", customerId);
        Page<CartDto> cartsDto = cartService.listCustomerCarts(customerId, pageable);
        return ResponseEntity.ok(cartsDto);
    }

    /**
     * Lista todos os carrinhos.
     * GET /api/v1/carts
     */
    @GetMapping
    public ResponseEntity<Page<CartDto>> listCarts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Listando todos os carrinhos");
        Page<CartDto> cartsDto = cartService.listCarts(pageable);
        return ResponseEntity.ok(cartsDto);
    }

    /**
     * Adiciona um item ao carrinho.
     * POST /api/v1/carts/{cartId}/items
     */
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartDto> addItem(
        @PathVariable UUID cartId,
        @Valid @RequestBody CreateCartItemDto itemDto
    ) {
        log.info("Adicionando item ao carrinho: {}", cartId);
        CartDto cartDto = cartService.addItem(cartId, itemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }

    /**
     * Remove um item do carrinho.
     * DELETE /api/v1/carts/{cartId}/items/{itemId}
     */
    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartDto> removeItem(
        @PathVariable UUID cartId,
        @PathVariable UUID itemId
    ) {
        log.info("Removendo item: {} do carrinho: {}", itemId, cartId);
        CartDto cartDto = cartService.removeItem(cartId, itemId);
        return ResponseEntity.ok(cartDto);
    }

    /**
     * Atualiza a quantidade de um item no carrinho.
     * PATCH /api/v1/carts/{cartId}/items/{itemId}
     */
    @PatchMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartDto> updateItemQuantity(
        @PathVariable UUID cartId,
        @PathVariable UUID itemId,
        @RequestParam Integer quantity
    ) {
        log.info("Atualizando quantidade do item: {} no carrinho: {}", itemId, cartId);
        CartDto cartDto = cartService.updateItemQuantity(cartId, itemId, quantity);
        return ResponseEntity.ok(cartDto);
    }

    /**
     * Limpa todos os itens do carrinho.
     * DELETE /api/v1/carts/{cartId}/items
     */
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<CartDto> clearCart(@PathVariable UUID cartId) {
        log.info("Limpando carrinho: {}", cartId);
        CartDto cartDto = cartService.clearCart(cartId);
        return ResponseEntity.ok(cartDto);
    }

    /**
     * Deleta um carrinho.
     * DELETE /api/v1/carts/{cartId}
     */
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable UUID cartId) {
        log.info("Deletando carrinho: {}", cartId);
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}

