package com.softserve.academy.studhub.security.services.impl;

import com.softserve.academy.studhub.security.model.PasswordResetToken;
import com.softserve.academy.studhub.security.repository.PasswordResetTokenRepository;
import com.softserve.academy.studhub.security.services.PasswordResetTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        return passwordResetTokenRepository.saveAndFlush(passwordResetToken);
    }

    @Override
    public void delete(PasswordResetToken passwordResetToken) {
        passwordResetTokenRepository.delete(passwordResetToken);
    }
}
