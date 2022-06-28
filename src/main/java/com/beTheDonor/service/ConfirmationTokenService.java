package com.beTheDonor.service;


import com.beTheDonor.repository.ConfirmationTokenRepository;
import com.beTheDonor.entity.UserConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService
{
    @Autowired
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(UserConfirmationToken token)
    {
        confirmationTokenRepository.save(token);
    }


    public Optional<UserConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token)
    {
        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

}
