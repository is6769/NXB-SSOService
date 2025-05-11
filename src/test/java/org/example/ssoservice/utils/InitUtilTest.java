package org.example.ssoservice.utils;

import org.example.ssoservice.entities.AppRole;
import org.example.ssoservice.entities.AppUser;
import org.example.ssoservice.repositories.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InitUtilTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private InitUtil initUtil;

    @Test
    void run_whenNoManagers_createsManagerUser() throws Exception {
        when(appUserRepository.findAllByRole(AppRole.ROLE_MANAGER)).thenReturn(Collections.emptyList());
        
        initUtil.run();

        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(userCaptor.capture());
        
        AppUser savedUser = userCaptor.getValue();
        assertEquals("-77777777777", savedUser.getMsisdn());
        assertEquals(AppRole.ROLE_MANAGER, savedUser.getRole());
        assertNull(savedUser.getReferenceId());
    }

    @Test
    void run_whenManagersExist_doesNotCreateManagerUser() throws Exception {
        List<AppUser> existingManagers = List.of(
            AppUser.builder()
                .id(1L)
                .msisdn("-77777777777")
                .role(AppRole.ROLE_MANAGER)
                .build()
        );
        
        when(appUserRepository.findAllByRole(AppRole.ROLE_MANAGER)).thenReturn(existingManagers);
        
        initUtil.run();
        
        verify(appUserRepository, never()).save(any());
    }
}
