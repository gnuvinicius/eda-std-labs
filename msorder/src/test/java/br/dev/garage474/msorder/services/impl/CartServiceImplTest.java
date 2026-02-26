package br.dev.garage474.msorder.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.dev.garage474.msorder.dto.CartDto;
import br.dev.garage474.msorder.dto.CreateCartDto;
import br.dev.garage474.msorder.models.Cart;
import br.dev.garage474.msorder.models.CartStatus;
import br.dev.garage474.msorder.repositories.CartRepository;

/**
 * Testes unitários para o serviço de carrinhos.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CartServiceImpl Tests")
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID customerId = UUID.randomUUID();
    private final UUID cartId = UUID.randomUUID();

    @Test
    @DisplayName("Deve criar um novo carrinho com sucesso")
    void testCreateCartSuccess() {
        // Arrange
        CreateCartDto dto = CreateCartDto.builder()
            .customerId(customerId)
            .build();

        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setCustomerId(customerId);
        cart.setStatus(CartStatus.ACTIVE);
        cart.setTenantId(tenantId);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Act
        CartDto result = cartService.createCart(tenantId, dto);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
        assertEquals(CartStatus.ACTIVE.toString(), result.getStatus());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("Deve buscar um carrinho por ID com sucesso")
    void testGetCartByIdSuccess() {
        // Arrange
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setCustomerId(customerId);
        cart.setStatus(CartStatus.ACTIVE);
        cart.setTenantId(tenantId);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());

        when(cartRepository.findByIdAndTenantId(cartId, tenantId))
            .thenReturn(Optional.of(cart));

        // Act
        CartDto result = cartService.getCartById(tenantId, cartId);

        // Assert
        assertNotNull(result);
        assertEquals(cartId, result.getId());
        verify(cartRepository).findByIdAndTenantId(cartId, tenantId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar carrinho inexistente")
    void testGetCartByIdNotFound() {
        // Arrange
        when(cartRepository.findByIdAndTenantId(cartId, tenantId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> cartService.getCartById(tenantId, cartId));
    }
}

