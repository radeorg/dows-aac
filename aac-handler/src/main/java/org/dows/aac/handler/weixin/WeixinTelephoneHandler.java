package org.dows.aac.handler.weixin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.ApiHandler;
import org.dows.aac.api.ApiMapping;
import org.dows.aac.constant.OpenApiEnum;
import org.dows.aac.weixin.GetTelephoneRequest;
import org.dows.rade.constant.OpenChannel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-info/phone-number/getPhoneNumber.html
 */
@RequiredArgsConstructor
@Slf4j
@Component
@ApiMapping(channel = OpenChannel.WEIXIN, func = OpenApiEnum.GET_TELEPHONE)
public class WeixinTelephoneHandler extends AbstractWeixinHandler implements ApiHandler {
    //POST https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=ACCESS_TOKEN
    private String URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=%s";

    @Override
    public Object processInputs(Object inputs) {
        return inputs;
    }

    @Override
    public <T> T processOutput(Object result, Class<T> outputClass) {
        return BeanUtil.copyProperties(result, outputClass);
    }

    /**
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-info/phone-number/getPhoneNumber.html
     *
     * @param input
     * @param outputClass
     * @param <T>
     * @return
     */
    @Override
    public <T> T execute(Object input, Class<T> outputClass) {
        //Object in = processInputs(input);
        //verifyOpenSettingByCurrentAppId();
        GetTelephoneRequest in = (GetTelephoneRequest) input;
        String uri = String.format(URL, in.getAccess_token());
        Map<String, Object> params = new HashMap<>();
        params.put("code", in.getCode());
        String post = HttpUtil.post(uri, JSONUtil.toJsonStr(params));
        //DocumentContext jsonContext = JsonPath.parse(json);
        return JSONUtil.toBean(post, outputClass);
    }
}
