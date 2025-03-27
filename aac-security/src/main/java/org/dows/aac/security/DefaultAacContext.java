package org.dows.aac.security;

import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.AacContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultAacContext implements AacContext {
    private static ThreadLocal<String> APP_ID_THREAD_LOCAL = new ThreadLocal<>();

    public void setAppId(String appId) {
        APP_ID_THREAD_LOCAL.set(appId);
    }

    public String getAppId() {
        return APP_ID_THREAD_LOCAL.get();
    }

    public void clear() {
        APP_ID_THREAD_LOCAL.remove();
    }
}
