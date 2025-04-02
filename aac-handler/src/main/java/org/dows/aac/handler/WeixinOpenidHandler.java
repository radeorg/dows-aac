package org.dows.aac.handler;

import cn.hutool.http.HttpUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.AacContext;
import org.dows.aac.api.ApiHandler;
import org.dows.aac.api.ApiMapping;
import org.dows.aac.api.constant.OpenApiEnum;
import org.dows.aac.api.constant.OpenChannel;
import org.dows.aac.yml.OpenProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@ApiMapping(channel = OpenChannel.WEIXIN, func = OpenApiEnum.GET_OPENID)
public class WeixinOpenidHandler implements ApiHandler {

    private static String URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%$js_code=%s&grant_type=authorization_code";

    private final List<OpenProperties> openProperties;

    private final AacContext aacContext;

    Map<String, OpenProperties> appIdPropertiesMap = new HashMap<>();

    //@Value("${wx.appId}")
    //private String appId;

    @PostConstruct
    public void init() {
        appIdPropertiesMap = openProperties.stream().collect(Collectors.toMap(OpenProperties::getAppId, Function.identity()));
        /*OpenProperties openProperties1 = openProperties.stream()
                .filter(o -> o.getAppId().equals(aacContext.getAppId())).findFirst()
                .orElse(null);
        if (openProperties1 == null) {
            log.info("初始化微信登录处理器");
        }*/
    }

    private String extractOpenidFromResponseBody(String responseBody) {
        int startIndex = responseBody.indexOf("\"openid\":\"") + "\"openid\":\"".length();
        int endIndex = responseBody.indexOf("\"", startIndex);
        return responseBody.substring(startIndex, endIndex);
    }

//    @Override
//    public Object processInputs(Object inputs) {
//
//        return null;
//    }
//
//    @Override
//    public <T> T processOutput(Object result, Class<T> outputClass) {
//        return null;
//    }

    @Override
    public <T> T execute(Object input, Class<T> outputClass) {
        Object in = processInputs(input);
        Map<String, Object> stringObjectMap = new HashMap<>();
        OpenProperties openProperties = appIdPropertiesMap.get(aacContext.getAppId());
        String uri = String.format(URL, openProperties.getThirdAppId(), openProperties.getSecret(), input.toString());
        String post = HttpUtil.post(uri, stringObjectMap);
        return processOutput(post, outputClass);
    }
}
