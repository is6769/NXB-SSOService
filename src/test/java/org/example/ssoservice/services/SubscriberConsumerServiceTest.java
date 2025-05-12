package org.example.ssoservice.services;

import org.example.ssoservice.dtos.NewSubscriberDTO;
import org.example.ssoservice.entities.AppRole;
import org.example.ssoservice.entities.AppUser;
import org.example.ssoservice.repositories.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * Тесты для класса {@link SubscriberConsumerService}.
 * Проверяют корректность обработки сообщений о новых абонентах.
 */
@ExtendWith(MockitoExtension.class)
class SubscriberConsumerServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private SubscriberConsumerService subscriberConsumerService;

    @Test
    void consumeSubscriber_savesNewAppUser() {
        NewSubscriberDTO newSubscriberDTO = new NewSubscriberDTO(1L, "79001234567");

        subscriberConsumerService.consumeSubscriber(newSubscriberDTO);

        ArgumentCaptor<AppUser> appUserCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(appUserCaptor.capture());

        AppUser savedUser = appUserCaptor.getValue();
        assertEquals(newSubscriberDTO.msisdn(), savedUser.getMsisdn());
        assertEquals(newSubscriberDTO.subscriberId(), savedUser.getReferenceId());
        assertEquals(AppRole.ROLE_SUBSCRIBER, savedUser.getRole());
    }
}
