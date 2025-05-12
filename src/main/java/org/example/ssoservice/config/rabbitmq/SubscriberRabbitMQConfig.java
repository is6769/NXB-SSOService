package org.example.ssoservice.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация RabbitMQ для очередей и обменников, связанных с созданием новых абонентов.
 * Определяет основную очередь для сообщений о новых абонентах и соответствующую Dead Letter Queue (DLQ).
 */
@Configuration
public class SubscriberRabbitMQConfig {

    /**
     * Имя очереди для сообщений о создании новых абонентов.
     */
    @Value("${const.rabbitmq.subscriber.SUBSCRIBER_CREATED_QUEUE_NAME}")
    private String SUBSCRIBER_CREATED_QUEUE_NAME;

    /**
     * Имя обменника для сообщений о создании новых абонентов.
     */
    @Value("${const.rabbitmq.subscriber.SUBSCRIBER_CREATED_EXCHANGE_NAME}")
    private String SUBSCRIBER_CREATED_EXCHANGE_NAME;

    /**
     * Ключ маршрутизации для сообщений о создании новых абонентов.
     */
    @Value("${const.rabbitmq.subscriber.SUBSCRIBER_CREATED_ROUTING_KEY}")
    private String SUBSCRIBER_CREATED_ROUTING_KEY;

    /**
     * Постфикс для имени обменника DLQ.
     */
    @Value("${const.rabbitmq.dead-letter.DEAD_LETTER_EXCHANGE_POSTFIX}")
    private String DEAD_LETTER_EXCHANGE_POSTFIX;

    /**
     * Постфикс для ключа маршрутизации DLQ.
     */
    @Value("${const.rabbitmq.dead-letter.DEAD_LETTER_ROUTING_KEY_POSTFIX}")
    private String DEAD_LETTER_ROUTING_KEY_POSTFIX;

    /**
     * Постфикс для имени очереди DLQ.
     */
    @Value("${const.rabbitmq.dead-letter.DEAD_LETTER_QUEUE_POSTFIX}")
    private String DEAD_LETTER_QUEUE_POSTFIX;


    /**
     * Создает бин для обменника Dead Letter Exchange (DLX) для очереди создания абонентов.
     * @return Объект {@link DirectExchange} для DLX.
     */
    @Bean
    public DirectExchange deadLetterSubscriberCreatedExchange(){
        return new DirectExchange(SUBSCRIBER_CREATED_EXCHANGE_NAME+DEAD_LETTER_EXCHANGE_POSTFIX,false,false);
    }

    /**
     * Создает бин для Dead Letter Queue (DLQ) для очереди создания абонентов.
     * @return Объект {@link Queue} для DLQ.
     */
    @Bean
    public Queue deadLetterSubscriberCreatedQueue(){
        return new Queue(SUBSCRIBER_CREATED_QUEUE_NAME+DEAD_LETTER_QUEUE_POSTFIX);
    }

    /**
     * Создает бин для привязки DLQ создания абонентов к ее обменнику DLX.
     * @return Объект {@link Binding}.
     */
    @Bean
    public Binding deadLetterSubscriberCreatedBinding(){
        return BindingBuilder
                .bind(deadLetterSubscriberCreatedQueue())
                .to(deadLetterSubscriberCreatedExchange())
                .with(SUBSCRIBER_CREATED_ROUTING_KEY+DEAD_LETTER_ROUTING_KEY_POSTFIX);
    }

    /**
     * Создает бин для основного обменника сообщений о создании новых абонентов.
     * @return Объект {@link DirectExchange}.
     */
    @Bean
    public DirectExchange subscriberCreatedExchange(){
        return new DirectExchange(SUBSCRIBER_CREATED_EXCHANGE_NAME,false,false);
    }

    /**
     * Создает бин для основной очереди сообщений о создании новых абонентов.
     * @return Объект {@link Queue}.
     */
    @Bean
    public Queue subscriberCreatedQueue(){
        return new Queue(SUBSCRIBER_CREATED_QUEUE_NAME);
    }

    /**
     * Создает бин для привязки основной очереди создания абонентов к ее обменнику.
     * @return Объект {@link Binding}.
     */
    @Bean
    public Binding subscriberWithTariffCreatedBinding(){
        return BindingBuilder
                .bind(subscriberCreatedQueue())
                .to(subscriberCreatedExchange())
                .with(SUBSCRIBER_CREATED_ROUTING_KEY);
    }

}
