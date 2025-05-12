package org.example.ssoservice.dtos;

/**
 * DTO (Data Transfer Object) для передачи информации о новом абоненте.
 * Используется при получении сообщений из RabbitMQ о создании нового абонента.
 *
 * @param subscriberId Уникальный идентификатор абонента (например, из BRT).
 * @param msisdn MSISDN (номер телефона) нового абонента.
 */
public record NewSubscriberDTO(
        Long subscriberId,
        String msisdn
) {
}
