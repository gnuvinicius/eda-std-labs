package br.dev.garage474.msdelivery.broker;

import br.dev.garage474.msdelivery.resources.dtos.CartCheckoutEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitCartCheckoutPublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitCartCheckoutPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String orderEventsExchange;
    private final String createdOrderRoutingKey;
    private final boolean publishEnabled;

    public RabbitCartCheckoutPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${delivery.rabbit.exchange}") String orderEventsExchange,
            @Value("${delivery.rabbit.routing-key}") String createdOrderRoutingKey,
            @Value("${delivery.rabbit.publish-enabled:false}") boolean publishEnabled
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderEventsExchange = orderEventsExchange;
        this.createdOrderRoutingKey = createdOrderRoutingKey;
        this.publishEnabled = publishEnabled;
    }

    public void publish(CartCheckoutEvent event) {
        if (!publishEnabled) {
            log.info("checkout publish is disabled, event prepared for orderId={}", event.getOrderId());
            return;
        }

        try {
            rabbitTemplate.convertAndSend(orderEventsExchange, createdOrderRoutingKey, event);
            log.info(
                    "checkout event published, orderId={} exchange={} routingKey={}",
                    event.getOrderId(),
                    orderEventsExchange,
                    createdOrderRoutingKey
            );
        } catch (Exception e) {
            log.error("failed to publish checkout event, orderId={}: {}", event.getOrderId(), e.getMessage(), e);
            throw e;
        }
    }
}

