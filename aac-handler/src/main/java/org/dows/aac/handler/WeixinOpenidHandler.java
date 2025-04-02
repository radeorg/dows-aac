package org.dows.aac.handler;

import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.ApiHandler;
import org.dows.aac.api.ApiMapping;
import org.dows.aac.api.constant.OpenApiEnum;
import org.dows.aac.api.constant.OpenChannel;

@Slf4j
@ApiMapping(channel = OpenChannel.WEIXIN, func = OpenApiEnum.GET_OPENID)
public class WeixinOpenidHandler implements ApiHandler {
    private String extractOpenidFromResponseBody(String responseBody) {
        int startIndex = responseBody.indexOf("\"openid\":\"") + "\"openid\":\"".length();
        int endIndex = responseBody.indexOf("\"", startIndex);
        return responseBody.substring(startIndex, endIndex);
    }

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
        Object o = processInputs(inputs);

        return null;
    }
}
