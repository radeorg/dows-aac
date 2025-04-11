package org.dows.aac.handler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.ApiHandler;
import org.dows.aac.api.ApiMapping;
import org.dows.aac.constant.IdentifierType;
import org.dows.aac.constant.OpenApiEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class HandlerDispatcher {

    private final Map<String, ApiHandler> apiHandlerMap;

    private final Map<String, ApiHandler> channelApiHandlerMap = new HashMap<>();

    @PostConstruct
    public void init() {
        apiHandlerMap.forEach((k, v) -> {
            ApiMapping apiMapping = v.getClass().getAnnotation(ApiMapping.class);
            if (apiMapping == null) {
                return;
            }
            String funcName = String.format("%s_%s", apiMapping.channel().name(), apiMapping.func().name());
            channelApiHandlerMap.put(funcName, v);
        });
    }

    public ApiHandler getHandler(IdentifierType identifierType, OpenApiEnum openApiEnum) {
        String channel = identifierType.getChannel();
        String funcName = String.format("%s_%s", channel, openApiEnum.name());
        return channelApiHandlerMap.get(funcName);
    }

}
