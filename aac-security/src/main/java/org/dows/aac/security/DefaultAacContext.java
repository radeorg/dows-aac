package org.dows.aac.security;

import lombok.extern.slf4j.Slf4j;
import org.dows.aac.constant.AuthStatusCode;
import org.dows.aac.exception.AacException;
import org.dows.rade.aac.AacContext;
import org.dows.rade.aac.AacUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultAacContext implements AacContext {

    @Override
    public AacUser getAacUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            throw new AacException(AuthStatusCode.UNAUTHORIZED);
        }
        return (AacUser) authentication.getPrincipal();
    }
}
