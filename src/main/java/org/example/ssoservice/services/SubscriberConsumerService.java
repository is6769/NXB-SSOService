package org.example.ssoservice.services;

import org.example.ssoservice.dtos.NewSubscriberDTO;
import org.example.ssoservice.entities.AppRole;
import org.example.ssoservice.entities.AppUser;
import org.example.ssoservice.repositories.AppUserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SubscriberConsumerService {

    private final AppUserRepository appUserRepository;

    public SubscriberConsumerService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @RabbitListener(queues = "${const.rabbitmq.subscriber.SUBSCRIBER_CREATED_QUEUE_NAME}")
    public void consumeSubscriber(NewSubscriberDTO newSubscriberDTO){
        AppUser newAppUser = AppUser.builder()
                .msisdn(newSubscriberDTO.msisdn())
                .role(AppRole.ROLE_SUBSCRIBER)
                .referenceId(newSubscriberDTO.subscriberId())
                .build();
        appUserRepository.save(newAppUser);
    }
}
