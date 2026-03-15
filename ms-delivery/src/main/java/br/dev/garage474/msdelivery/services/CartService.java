package br.dev.garage474.msdelivery.services;

import br.dev.garage474.msdelivery.dtos.*;
import br.dev.garage474.msdelivery.exceptions.CartAlreadyCheckedOutException;
import br.dev.garage474.msdelivery.exceptions.CartNotFoundException;
import br.dev.garage474.msdelivery.exceptions.EmptyCartCheckoutException;
import br.dev.garage474.msdelivery.models.Cart;
import br.dev.garage474.msdelivery.models.CartItem;
import br.dev.garage474.msdelivery.models.CartStatus;
import br.dev.garage474.msdelivery.repositories.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartCheckoutPublisher cartCheckoutPublisher;

    public CartService(
            CartRepository cartRepository,
            CartMapper cartMapper,
            CartCheckoutPublisher cartCheckoutPublisher
    ) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.cartCheckoutPublisher = cartCheckoutPublisher;
    }

    @Transactional
    public CartResponse createCart(CreateCartRequest request) {
        try {
            Cart cart = new Cart();
            cart.setId(UUID.randomUUID());
            cart.setCustomerId(request.customerId());
            cart.setStatus(CartStatus.OPEN);

            Cart savedCart = cartRepository.save(cart);
            log.info("cart created with id={} customerId={}", savedCart.getId(), savedCart.getCustomerId());
            return cartMapper.toResponse(savedCart);
        } catch (Exception e) {
            log.error("error creating cart for customerId={}: {}", request.customerId(), e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public CartResponse addItem(UUID cartId, AddCartItemRequest request) {
        try {
            Cart cart = getOpenCart(cartId);

            CartItem cartItem = new CartItem();
            cartItem.setProductId(request.productId());
            cartItem.setProductName(request.productName());
            cartItem.setQuantity(request.quantity());
            cartItem.setUnitPrice(request.unitPrice());

            cart.addItem(cartItem);
            Cart savedCart = cartRepository.save(cart);

            log.info("item added to cartId={} productId={} quantity={}", cartId, request.productId(), request.quantity());
            return cartMapper.toResponse(savedCart);
        } catch (Exception e) {
            log.error("error adding item to cartId={}: {}", cartId, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public CheckoutCartResponse checkout(UUID cartId) {
        try {
            Cart cart = getOpenCart(cartId);
            if (cart.getItems().isEmpty()) {
                throw new EmptyCartCheckoutException(cartId);
            }

            cart.setStatus(CartStatus.CHECKED_OUT);
            cart.setCheckedOutAt(LocalDateTime.now());
            Cart savedCart = cartRepository.save(cart);

            CartCheckoutEvent event = cartMapper.toCheckoutEvent(savedCart);
            cartCheckoutPublisher.publish(event);

            log.info("cart checked out with id={} and event sent to processing", cartId);
            return new CheckoutCartResponse(cartId, CartStatus.CHECKED_OUT.name(), "cart sent to processing queue");
        } catch (Exception e) {
            log.error("error checking out cartId={}: {}", cartId, e.getMessage(), e);
            throw e;
        }
    }

    private Cart getOpenCart(UUID cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        if (cart.isCheckedOut()) {
            throw new CartAlreadyCheckedOutException(cartId);
        }
        return cart;
    }
}

