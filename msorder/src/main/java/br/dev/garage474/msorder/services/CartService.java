package br.dev.garage474.msorder.services;

import br.dev.garage474.msorder.dto.CartDto;
import br.dev.garage474.msorder.dto.CreateCartDto;
import br.dev.garage474.msorder.dto.CreateCartItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Interface de serviço para gerenciamento de carrinhos.
 */
public interface CartService {

    /**
     * Cria um novo carrinho.
     */
    CartDto createCart(UUID tenantId, CreateCartDto dto);

    /**
     * Busca um carrinho por ID.
     */
    CartDto getCartById(UUID tenantId, UUID cartId);

    /**
     * Lista os carrinhos ativos de um cliente.
     */
    Page<CartDto> listCustomerCarts(UUID tenantId, UUID customerId, Pageable pageable);

    /**
     * Lista todos os carrinhos de um tenant.
     */
    Page<CartDto> listCarts(UUID tenantId, Pageable pageable);

    /**
     * Adiciona um item ao carrinho.
     */
    CartDto addItem(UUID tenantId, UUID cartId, CreateCartItemDto itemDto);

    /**
     * Remove um item do carrinho.
     */
    CartDto removeItem(UUID tenantId, UUID cartId, UUID itemId);

    /**
     * Atualiza a quantidade de um item no carrinho.
     */
    CartDto updateItemQuantity(UUID tenantId, UUID cartId, UUID itemId, Integer quantity);

    /**
     * Limpa todos os itens do carrinho.
     */
    CartDto clearCart(UUID tenantId, UUID cartId);

    /**
     * Remove um carrinho.
     */
    void deleteCart(UUID tenantId, UUID cartId);
}

