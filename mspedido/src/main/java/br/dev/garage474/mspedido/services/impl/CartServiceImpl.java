package br.dev.garage474.mspedido.services.impl;

import br.dev.garage474.mspedido.dto.CartDto;
import br.dev.garage474.mspedido.dto.CreateCartDto;
import br.dev.garage474.mspedido.dto.CreateCartItemDto;
import br.dev.garage474.mspedido.models.Cart;
import br.dev.garage474.mspedido.models.CartItem;
import br.dev.garage474.mspedido.models.CartStatus;
import br.dev.garage474.mspedido.models.Money;
import br.dev.garage474.mspedido.repositories.CartRepository;
import br.dev.garage474.mspedido.services.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementação do serviço de gerenciamento de carrinhos.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    @Transactional
    public CartDto createCart(UUID tenantId, CreateCartDto dto) {
        try {
            log.info("Criando novo carrinho para tenant: {} e customer: {}", tenantId, dto.getCustomerId());

            Cart cart = new Cart();
            cart.setId(UUID.randomUUID());
            cart.setCustomerId(dto.getCustomerId());
            cart.setStatus(CartStatus.ACTIVE);
            cart.setTenantId(tenantId);

            Cart savedCart = cartRepository.save(cart);
            log.info("Carrinho criado com sucesso: {}", savedCart.getId());

            return CartDto.mapToDto(savedCart);
        } catch (Exception e) {
            log.error("Erro ao criar carrinho: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CartDto getCartById(UUID tenantId, UUID cartId) {
        try {
            log.info("Buscando carrinho: {} para tenant: {}", cartId, tenantId);

            Cart cart = cartRepository.findByIdAndTenantId(cartId, tenantId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

            return CartDto.mapToDto(cart);
        } catch (Exception e) {
            log.error("Erro ao buscar carrinho {}: {}", cartId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CartDto> listCustomerCarts(UUID tenantId, UUID customerId, Pageable pageable) {
        try {
            log.info("Listando carrinhos do cliente: {} para tenant: {}", customerId, tenantId);

            return cartRepository.findByTenantIdAndCustomerId(tenantId, customerId, pageable)
                .map(CartDto::mapToDto);
        } catch (Exception e) {
            log.error("Erro ao listar carrinhos do cliente: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CartDto> listCarts(UUID tenantId, Pageable pageable) {
        try {
            log.info("Listando todos os carrinhos para tenant: {}", tenantId);

            return cartRepository.findByTenantId(tenantId, pageable)
                .map(CartDto::mapToDto);
        } catch (Exception e) {
            log.error("Erro ao listar carrinhos: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public CartDto addItem(UUID tenantId, UUID cartId, CreateCartItemDto itemDto) {
        try {
            log.info("Adicionando item ao carrinho: {}", cartId);

            Cart cart = cartRepository.findByIdAndTenantId(cartId, tenantId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

            CartItem cartItem = new CartItem();
            cartItem.setId(UUID.randomUUID());
            cartItem.setProductId(itemDto.getProductId());
            cartItem.setProductVariantId(itemDto.getProductVariantId());
            cartItem.setQuantity(itemDto.getQuantity());
            cartItem.setUnitPrice(new Money(itemDto.getUnitPriceAmount(), itemDto.getUnitPriceCurrency()));
            cartItem.setTenantId(tenantId);

            cart.addItem(cartItem);
            Cart updatedCart = cartRepository.save(cart);

            log.info("Item adicionado ao carrinho com sucesso: {}", cartId);
            return CartDto.mapToDto(updatedCart);
        } catch (Exception e) {
            log.error("Erro ao adicionar item ao carrinho: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public CartDto removeItem(UUID tenantId, UUID cartId, UUID itemId) {
        try {
            log.info("Removendo item: {} do carrinho: {}", itemId, cartId);

            Cart cart = cartRepository.findByIdAndTenantId(cartId, tenantId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

            CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item não encontrado no carrinho"));

            cart.removeItem(itemToRemove);
            Cart updatedCart = cartRepository.save(cart);

            log.info("Item removido do carrinho com sucesso: {}", cartId);
            return CartDto.mapToDto(updatedCart);
        } catch (Exception e) {
            log.error("Erro ao remover item do carrinho: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public CartDto updateItemQuantity(UUID tenantId, UUID cartId, UUID itemId, Integer quantity) {
        try {
            log.info("Atualizando quantidade do item: {} no carrinho: {}", itemId, cartId);

            Cart cart = cartRepository.findByIdAndTenantId(cartId, tenantId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

            CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item não encontrado no carrinho"));

            item.setQuantity(quantity);
            Cart updatedCart = cartRepository.save(cart);

            log.info("Quantidade do item atualizada com sucesso: {}", cartId);
            return CartDto.mapToDto(updatedCart);
        } catch (Exception e) {
            log.error("Erro ao atualizar quantidade do item: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public CartDto clearCart(UUID tenantId, UUID cartId) {
        try {
            log.info("Limpando carrinho: {}", cartId);

            Cart cart = cartRepository.findByIdAndTenantId(cartId, tenantId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

            cart.clearItems();
            Cart updatedCart = cartRepository.save(cart);

            log.info("Carrinho limpo com sucesso: {}", cartId);
            return CartDto.mapToDto(updatedCart);
        } catch (Exception e) {
            log.error("Erro ao limpar carrinho: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteCart(UUID tenantId, UUID cartId) {
        try {
            log.info("Deletando carrinho: {}", cartId);

            Cart cart = cartRepository.findByIdAndTenantId(cartId, tenantId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

            cartRepository.delete(cart);
            log.info("Carrinho deletado com sucesso: {}", cartId);
        } catch (Exception e) {
            log.error("Erro ao deletar carrinho: {}", e.getMessage(), e);
            throw e;
        }
    }



}

