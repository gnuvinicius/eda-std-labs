package br.dev.garage474.mspedido.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de filas e exchanges do RabbitMQ para processamento de pedidos.
 */
@Configuration
public class RabbitMQConfig {

    // Constantes de configuração
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDER_ROUTING_KEY = "order.created";

    /**
     * Define o exchange direto para pedidos.
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE, true, false);
    }

    /**
     * Define a fila para processamento de pedidos.
     */
    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true, false, false);
    }

    /**
     * Faz o binding entre a fila e o exchange.
     */
    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue)
            .to(orderExchange)
            .with(ORDER_ROUTING_KEY);
    }
}

