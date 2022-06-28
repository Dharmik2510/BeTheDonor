package com.beTheDonor.service;

import com.beTheDonor.entity.UserConfirmationToken;
import com.beTheDonor.repository.ConfirmationTokenRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConfirmationTokenServiceTest {

    @InjectMocks
    ConfirmationTokenService confirmationTokenService;

    @Mock
    ConfirmationTokenRepository confirmationTokenRepository;

    @Mock
    UserConfirmationToken token;

    @Test
    @DisplayName("Testing save confirmation token ")
    @Order(1)
    void saveConfirmationToken()
    {
        UserConfirmationToken token = getUserConfirmationToken();
        when(confirmationTokenRepository.save(token)).thenReturn(null);
        confirmationTokenService.saveConfirmationToken(token);
        verify(confirmationTokenRepository).save(token);
    }

    @Test
    @DisplayName("Testing getToken() method678")
    @Order(2)
    void getToken()
    {
        UserConfirmationToken expected = getUserConfirmationToken();
        when(confirmationTokenRepository.findByToken(anyString())).thenReturn(Optional.of(expected));

        Optional<UserConfirmationToken> token = confirmationTokenService.getToken(expected.getToken());

        assertNotNull(token);
        assertTrue(token.isPresent());
        assertSame(expected, token.get());
        verify(confirmationTokenRepository).findByToken(anyString());
    }

    @Test
    @DisplayName("Testing getToken() method678 return null")
    @Order(3)
    void getToken_whenReturnNull()
    {
        when(confirmationTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        Optional<UserConfirmationToken> token = confirmationTokenService.getToken("dummyToken");

        assertNotNull(token);
        assertTrue(token.isEmpty());
    }

    @Test
    @DisplayName("Testing setConfirmedAt() method")
    @Order(4)
    void setConfirmedAt()
    {
        when(confirmationTokenRepository.updateConfirmedAt(anyString(), any(LocalDateTime.class))).thenReturn(1);

        int res = confirmationTokenService.setConfirmedAt("dummyToken");

        assertEquals(1, res);
        verify(confirmationTokenRepository).updateConfirmedAt(anyString(), any(LocalDateTime.class));
    }

    private UserConfirmationToken getUserConfirmationToken() {
        return new UserConfirmationToken("dummyToken", LocalDateTime.now(), LocalDateTime.now().plusDays(1), null);
    }
}