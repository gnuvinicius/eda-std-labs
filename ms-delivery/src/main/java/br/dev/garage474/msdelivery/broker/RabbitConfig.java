package br.dev.garage474.msdelivery.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public FanoutExchange orderEventsExchange(@Value("${delivery.rabbit.exchange}") String exchangeName) {
        return new FanoutExchange(exchangeName, true, false);
    }

    @Bean
    public FanoutExchange orderEventsDeadLetterExchange(@Value("${delivery.rabbit.dlx}") String dlxName) {
        return new FanoutExchange(dlxName, true, false);
    }

    @Bean
    public MessageConverter rabbitMessageConverter(ObjectMapper objectMapper) {
        ObjectMapper mapper = objectMapper.copy();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public Queue orderNewQueue(
            @Value("${delivery.rabbit.queue}") String queueName,
            @Value("${delivery.rabbit.dlx}") String deadLetterExchange,
            @Value("${delivery.rabbit.dlq-routing-key}") String deadLetterRoutingKey
    ) {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", deadLetterRoutingKey)
                .build();
    }

    @Bean
    public Queue orderNewDlq(@Value("${delivery.rabbit.dlq}") String dlqName) {
        return QueueBuilder.durable(dlqName).build();
    }

    @Bean
    public Binding orderNewQueueBinding(
            @Qualifier("orderNewQueue") Queue orderNewQueue,
            @Qualifier("orderEventsExchange") TopicExchange orderEventsExchange,
            @Value("${delivery.rabbit.routing-key}") String routingKey
    ) {
        return BindingBuilder.bind(orderNewQueue).to(orderEventsExchange).with(routingKey);
    }

    @Bean
    public Binding orderNewDlqBinding(
            @Qualifier("orderNewDlq") Queue orderNewDlq,
            @Qualifier("orderEventsDeadLetterExchange") TopicExchange orderEventsDeadLetterExchange,
            @Value("${delivery.rabbit.dlq-routing-key}") String dlqRoutingKey
    ) {
        return BindingBuilder.bind(orderNewDlq).to(orderEventsDeadLetterExchange).with(dlqRoutingKey);
    }

}

