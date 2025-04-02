package org.dows.aac.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.AacSettings;
import org.dows.aac.api.AacContext;
import org.dows.aac.api.AacException;
import org.dows.aac.api.ApiHandler;
import org.dows.aac.api.ApiMapping;
import org.dows.aac.api.constant.OpenApiEnum;
import org.dows.aac.api.constant.OpenChannel;
import org.dows.aac.weixin.WxUserInfo;
import org.dows.aac.yml.OpenSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@ApiMapping(channel = OpenChannel.WEIXIN, func = OpenApiEnum.GET_OPENID)
public class WeixinOpenidHandler implements ApiHandler {

    private static String URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    private final AacSettings aacSettings;
    private final AacContext aacContext;

    private Map<String, OpenSetting> openSettingMap = new HashMap<>();


    @PostConstruct
    public void init() {
        List<OpenSetting> opens = aacSettings.getOpens();
        openSettingMap = opens.stream().collect(Collectors
                .toMap(OpenSetting::getAppId, Function.identity()));
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

    @Override
    public Object processInputs(Object inputs) {
        return inputs;
    }

    @Override
    public <T> T processOutput(Object result, Class<T> outputClass) {
        return BeanUtil.copyProperties(result, outputClass);
    }

    @Override
    public <T> T execute(Object input, Class<T> outputClass) {
        Object in = processInputs(input);
        OpenSetting openSetting = openSettingMap.get(aacContext.getAppId());
        if (openSetting == null) {
            throw new AacException("应用未配置");
        }
        String uri = String.format(URL, openSetting.getThirdAppId(), openSetting.getSecret(), in.toString());
        String post = HttpUtil.get(uri);
        //DocumentContext jsonContext = JsonPath.parse(json);
        WxUserInfo bean = JSONUtil.toBean(post, WxUserInfo.class);
        if (StrUtil.isBlank(bean.getOpenid())) {
            //return null;
            throw new AacException("微信登录失败");
        }
        // 对结果进行处理
        return processOutput(bean, outputClass);
    }
}
