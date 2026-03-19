package br.dev.garage474.msorder.mappers;

import br.dev.garage474.msorder.dtos.*;
import br.dev.garage474.msorder.entities.Address;
import br.dev.garage474.msorder.entities.Order;
import br.dev.garage474.msorder.entities.OrderItem;
import br.dev.garage474.msorder.entities.Payment;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper responsible for converting between Order-related DTOs and entities.
 */
@ApplicationScoped
public class OrderMapper {

    /**
     * Converts a {@link CreateOrderDto} to an {@link Order} entity.
     *
     * @param dto the DTO with the data to create the order
     * @return a new {@link Order} entity
     */
    public Order toEntity(CreateOrderDto dto) {
        if (dto == null) {
            return null;
        }

        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setCustomerNotes(dto.getCustomerNotes());
        order.setShippingCost(dto.getShippingCost() != null ? dto.getShippingCost() : BigDecimal.ZERO);
        order.setTotalDiscount(dto.getTotalDiscount() != null ? dto.getTotalDiscount() : BigDecimal.ZERO);
        order.setShippingAddress(toAddressEntity(dto.getShippingAddress()));
        order.setPayment(toPaymentEntity(dto.getPayment()));
        order.setItems(toOrderItemEntityList(dto.getItems()));

        return order;
    }

    /**
     * Converts an {@link Order} entity to a full {@link OrderDto} response with success=true.
     *
     * @param order the order entity to convert
     * @return an {@link OrderDto} with success flag set to true
     */
    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        return OrderDto.builder()
                .success(true)
                .id(order.getId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .items(toOrderItemDtoList(order.getItems()))
                .shippingAddress(toAddressDto(order.getShippingAddress()))
                .payment(toPaymentDto(order.getPayment()))
                .customerNotes(order.getCustomerNotes())
                .shippingCost(order.getShippingCost())
                .totalDiscount(order.getTotalDiscount())
                .subtotal(order.calculateSubtotal())
                .total(order.calculateTotal())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .deliveredAt(order.getDeliveredAt())
                .cancellationReason(order.getCancellationReason())
                .build();
    }

    /**
     * Converts an {@link AddressDto} to an {@link Address} entity.
     *
     * @param dto the address DTO
     * @return an {@link Address} entity, or null if dto is null
     */
    public Address toAddressEntity(AddressDto dto) {
        if (dto == null) {
            return null;
        }

        Address address = new Address();
        address.setId(dto.getId());
        address.setStreet(dto.getStreet());
        address.setNumber(dto.getNumber());
        address.setComplement(dto.getComplement());
        address.setNeighborhood(dto.getNeighborhood());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setZipCode(dto.getZipCode());
        address.setCountry(dto.getCountry());

        return address;
    }

    /**
     * Converts an {@link Address} entity to an {@link AddressDto}.
     *
     * @param address the address entity
     * @return an {@link AddressDto}, or null if address is null
     */
    public AddressDto toAddressDto(Address address) {
        if (address == null) {
            return null;
        }

        return AddressDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .country(address.getCountry())
                .build();
    }

    /**
     * Converts a {@link PaymentDto} to a {@link Payment} entity.
     *
     * @param dto the payment DTO
     * @return a {@link Payment} entity, or null if dto is null
     */
    public Payment toPaymentEntity(PaymentDto dto) {
        if (dto == null) {
            return null;
        }

        Payment payment = new Payment();
        payment.setId(dto.getId());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setTotalAmount(dto.getTotalAmount());
        payment.setPaymentStatus(dto.getPaymentStatus());
        payment.setTransactionId(dto.getTransactionId());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setPaymentNotes(dto.getPaymentNotes());

        return payment;
    }

    /**
     * Converts a {@link Payment} entity to a {@link PaymentDto}.
     *
     * @param payment the payment entity
     * @return a {@link PaymentDto}, or null if payment is null
     */
    public PaymentDto toPaymentDto(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentDto.builder()
                .id(payment.getId())
                .paymentMethod(payment.getPaymentMethod())
                .totalAmount(payment.getTotalAmount())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .paymentDate(payment.getPaymentDate())
                .paymentNotes(payment.getPaymentNotes())
                .build();
    }

    /**
     * Converts an {@link OrderItemDto} to an {@link OrderItem} entity.
     *
     * @param dto the order item DTO
     * @return an {@link OrderItem} entity, or null if dto is null
     */
    public OrderItem toOrderItemEntity(OrderItemDto dto) {
        if (dto == null) {
            return null;
        }

        OrderItem item = new OrderItem();
        item.setId(dto.getId());
        item.setProductId(dto.getProductId());
        item.setProductName(dto.getProductName());
        item.setQuantity(dto.getQuantity());
        item.setUnitPrice(dto.getUnitPrice());
        item.setDiscount(dto.getDiscount());

        return item;
    }

    /**
     * Converts an {@link OrderItem} entity to an {@link OrderItemDto}.
     *
     * @param item the order item entity
     * @return an {@link OrderItemDto}, or null if item is null
     */
    public OrderItemDto toOrderItemDto(OrderItem item) {
        if (item == null) {
            return null;
        }

        return OrderItemDto.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .discount(item.getDiscount())
                .build();
    }

    /**
     * Converts a list of {@link OrderItemDto} to a list of {@link OrderItem} entities.
     *
     * @param dtos the list of order item DTOs
     * @return list of {@link OrderItem} entities
     */
    public List<OrderItem> toOrderItemEntityList(List<OrderItemDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(this::toOrderItemEntity)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of {@link OrderItem} entities to a list of {@link OrderItemDto}.
     *
     * @param items the list of order item entities
     * @return list of {@link OrderItemDto}
     */
    public List<OrderItemDto> toOrderItemDtoList(List<OrderItem> items) {
        if (items == null) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toList());
    }
}

