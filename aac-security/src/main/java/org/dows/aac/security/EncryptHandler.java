package org.dows.aac.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.rade.encrypt.EncryptApi;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EncryptHandler implements EncryptApi {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String getBCryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
