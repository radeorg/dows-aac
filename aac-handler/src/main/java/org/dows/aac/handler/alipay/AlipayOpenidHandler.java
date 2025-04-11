package org.dows.aac.handler.alipay;

import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.ApiHandler;
import org.dows.aac.api.ApiMapping;
import org.dows.aac.constant.OpenApiEnum;
import org.dows.rade.constant.OpenChannel;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ApiMapping(channel = OpenChannel.ALIPAY, func = OpenApiEnum.GET_OPENID)
public class AlipayOpenidHandler implements ApiHandler {
    @Override
    public Object processInputs(Object inputs) {

        return null;
    }

    @Override
    public <T> T processOutput(Object result, Class<T> outputClass) {
        return null;
    }

    @Override
    public <T> T execute(Object inputs, Class<T> outputClass) {

        return null;
    }
}
