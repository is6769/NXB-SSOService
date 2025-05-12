package org.example.ssoservice.services;

import org.example.ssoservice.dtos.NewSubscriberDTO;
import org.example.ssoservice.entities.AppRole;
import org.example.ssoservice.entities.AppUser;
import org.example.ssoservice.repositories.AppUserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Сервис для обработки сообщений о новых абонентах из RabbitMQ.
 * Создает учетные записи для новых абонентов в системе SSO.
 */
@Service
public class SubscriberConsumerService {

    private final AppUserRepository appUserRepository;

    public SubscriberConsumerService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    /**
     * Обрабатывает сообщение о создании нового абонента.
     * Создает нового пользователя {@link AppUser} с ролью {@link AppRole#ROLE_SUBSCRIBER}
     * и сохраняет его в базе данных.
     *
     * @param newSubscriberDTO DTO с информацией о новом абоненте (ID и MSISDN).
     */
    @RabbitListener(queues = "${const.rabbitmq.subscriber.SUBSCRIBER_CREATED_QUEUE_NAME}", errorHandler = "rabbitExceptionsHandler")
    public void consumeSubscriber(NewSubscriberDTO newSubscriberDTO){
        AppUser newAppUser = AppUser.builder()
                .msisdn(newSubscriberDTO.msisdn())
                .role(AppRole.ROLE_SUBSCRIBER)
                .referenceId(newSubscriberDTO.subscriberId())
                .build();
        appUserRepository.save(newAppUser);
    }
}
