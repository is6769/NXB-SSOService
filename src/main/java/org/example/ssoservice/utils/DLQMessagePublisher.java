package org.example.ssoservice.utils;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Компонент для публикации сообщений в Dead Letter Queue (DLQ).
 * Используется для отправки сообщений, которые не удалось обработать, в специальную очередь
 * для последующего анализа или повторной обработки.
 */
@Component
public class DLQMessagePublisher {

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

    private final RabbitTemplate rabbitTemplate;

    public DLQMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Публикует исходное сообщение в соответствующую DLQ.
     * Обогащает сообщение информацией об исключении (тип и сообщение об ошибке).
     *
     * @param originalMessage Исходное сообщение, которое не удалось обработать.
     * @param cause Исключение, вызвавшее ошибку обработки.
     */
    public void publishToDLQ(Message originalMessage, Throwable cause){
        String originalExchange = originalMessage.getMessageProperties().getReceivedExchange();
        String originalRoutingKey = originalMessage.getMessageProperties().getReceivedRoutingKey();
        Message enhancedMessage = MessageBuilder
                .fromMessage(originalMessage)
                .setHeader("x-exception-type", cause.getClass().getName())
                .setHeader("x-error-msg", cause.getMessage())
                .build();

        rabbitTemplate.convertAndSend(originalExchange+DEAD_LETTER_EXCHANGE_POSTFIX,originalRoutingKey+DEAD_LETTER_ROUTING_KEY_POSTFIX,enhancedMessage);
    }
}
