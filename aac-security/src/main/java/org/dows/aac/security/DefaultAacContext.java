package org.dows.aac.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.AacSettings;
import org.dows.aac.constant.AuthStatusCode;
import org.dows.aac.exception.AacException;
import org.dows.rade.aac.AacContext;
import org.dows.rade.aac.AacUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class DefaultAacContext implements AacContext {

    //private final TentantAppApi tentantAppApi;

    private final AacSettings aacSettings;
    @Override
    public AacUser getAacUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            throw new AacException(AuthStatusCode.UNAUTHORIZED);
        }
        return (AacUser) authentication.getPrincipal();
    }


    public String getAppIdByNamespace(String namespace) {
        String appId = null;//tentantAppApi.getAppIdByNamespace(namespace);
        return appId;

    }

    public String[] getWhitelist(String appId) {
        String[] whitelist = aacSettings.getWhitelist();
        return whitelist;
    }
}
