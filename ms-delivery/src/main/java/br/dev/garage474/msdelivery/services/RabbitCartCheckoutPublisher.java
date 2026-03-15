package br.dev.garage474.msdelivery.services;

import br.dev.garage474.msdelivery.dtos.CartCheckoutEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitCartCheckoutPublisher implements CartCheckoutPublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitCartCheckoutPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String orderEventsExchange;
    private final String newOrderRoutingKey;
    private final boolean publishEnabled;

    public RabbitCartCheckoutPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${delivery.rabbit.exchange}") String orderEventsExchange,
            @Value("${delivery.rabbit.routing-key}") String newOrderRoutingKey,
            @Value("${delivery.rabbit.publish-enabled:false}") boolean publishEnabled
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderEventsExchange = orderEventsExchange;
        this.newOrderRoutingKey = newOrderRoutingKey;
        this.publishEnabled = publishEnabled;
    }

    @Override
    public void publish(CartCheckoutEvent event) {
        if (!publishEnabled) {
            log.info("checkout publish is disabled, event prepared for cartId={}", event.cartId());
            return;
        }

        try {
            rabbitTemplate.convertAndSend(orderEventsExchange, newOrderRoutingKey, event);
            log.info(
                    "checkout event published, cartId={} exchange={} routingKey={}",
                    event.cartId(),
                    orderEventsExchange,
                    newOrderRoutingKey
            );
        } catch (Exception e) {
            log.error("failed to publish checkout event, cartId={}: {}", event.cartId(), e.getMessage(), e);
            throw e;
        }
    }
}

