package org.dows.aac.security;

import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.AacContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultAacContext implements AacContext {
    @Override
    public String getAppId() {
        return "";
    }
}
