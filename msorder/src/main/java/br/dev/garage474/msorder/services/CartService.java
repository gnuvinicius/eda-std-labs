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
    CartDto createCart(CreateCartDto dto);

    /**
     * Busca um carrinho por ID.
     */
    CartDto getCartById(UUID cartId);

    /**
     * Lista os carrinhos ativos de um cliente.
     */
    Page<CartDto> listCustomerCarts(UUID customerId, Pageable pageable);

    /**
     * Lista todos os carrinhos.
     */
    Page<CartDto> listCarts(Pageable pageable);

    /**
     * Adiciona um item ao carrinho.
     */
    CartDto addItem(UUID cartId, CreateCartItemDto itemDto);

    /**
     * Remove um item do carrinho.
     */
    CartDto removeItem(UUID cartId, UUID itemId);

    /**
     * Atualiza a quantidade de um item no carrinho.
     */
    CartDto updateItemQuantity(UUID cartId, UUID itemId, Integer quantity);

    /**
     * Limpa todos os itens do carrinho.
     */
    CartDto clearCart(UUID cartId);

    /**
     * Remove um carrinho.
     */
    void deleteCart(UUID cartId);
}

