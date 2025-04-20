package org.dows.aac.handler.third;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.AacSettings;
import org.dows.aac.api.ApiHandler;
import org.dows.aac.api.ThirdPartyApi;
import org.dows.aac.constant.OpenApiEnum;
import org.dows.aac.exception.AacException;
import org.dows.aac.handler.HandlerDispatcher;
import org.dows.aac.request.GetAccessTokenRequest;
import org.dows.aac.request.ThirdPartyPreRegisterRequest;
import org.dows.aac.weixin.GetTelephoneRequest;
import org.dows.aac.weixin.GetTelephoneResponse;
import org.dows.aac.weixin.OpenidResponse;
import org.dows.aac.weixin.WeixinAccessToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ThirdPartyHandler implements ThirdPartyApi {
    private final AacSettings aacSettings;
    private final HandlerDispatcher handlerDispatcher;

    /**
     * 获取第三方用户的手机号
     *
     * @param thirdPartyPreRegisterRequest
     * @return
     */
    public GetTelephoneResponse getThirdPartyTelephone(ThirdPartyPreRegisterRequest thirdPartyPreRegisterRequest) {
        // 获取access_token
        ApiHandler tokenHandler = handlerDispatcher
                .getHandler(thirdPartyPreRegisterRequest.getIdentifierType(), OpenApiEnum.GET_ACCESS_TOKEN);
        if (tokenHandler == null) {
            throw new AacException(String.format("暂不支持%s获取信息", thirdPartyPreRegisterRequest.getIdentifierType()));
        }
        GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest();
        WeixinAccessToken weixinAccessToken = tokenHandler.execute(getAccessTokenRequest, WeixinAccessToken.class);
        if (weixinAccessToken.getErrcode() != null) {
            throw new AacException(weixinAccessToken.getErrmsg());
        }
        /**
         * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-info/phone-number/getPhoneNumber.html
         */
        // 根据access_token 获取手机号
        ApiHandler handler = handlerDispatcher
                .getHandler(thirdPartyPreRegisterRequest.getIdentifierType(), OpenApiEnum.GET_TELEPHONE);
        if (handler == null) {
            throw new AacException(String.format("暂不支持%s获取信息", thirdPartyPreRegisterRequest.getIdentifierType()));
        }
        GetTelephoneRequest getTelephoneRequest = new GetTelephoneRequest();
        getTelephoneRequest.setCode(thirdPartyPreRegisterRequest.getCodeForData());
        getTelephoneRequest.setAccess_token(weixinAccessToken.getAccess_token());
        GetTelephoneResponse getTelephoneResponse = handler.execute(getTelephoneRequest, GetTelephoneResponse.class);
        if (getTelephoneResponse.getErrcode() != 0) {
            if (!this.aacSettings.getLoginSetting().isTest()) {
                String logStr = String.format("获取微信信息绑定账号失败,错误码:%s,错误信息:%s",
                        getTelephoneResponse.getErrcode(), getTelephoneResponse.getErrmsg());
                log.error(logStr);
                throw new AacException(logStr);
            }
            GetTelephoneResponse.PhoneInfo phoneInfo = new GetTelephoneResponse.PhoneInfo();
            String telephone = thirdPartyPreRegisterRequest.getMockTelephone();
            if (telephone == null) {
                telephone = "13800138000";
            }
            phoneInfo.setPhoneNumber("+86" + telephone);
            phoneInfo.setPurePhoneNumber(telephone);
            phoneInfo.setCountryCode("86");
            getTelephoneResponse.setPhone_info(phoneInfo);
        }
        return getTelephoneResponse;
    }


    /**
     * 获取第三方用户的openid
     * 微信：https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
     * 支付宝：
     * 抖音：
     *
     * @param thirdPartyPreRegisterRequest
     * @return
     */
    public OpenidResponse getThirdPartyOpenid(ThirdPartyPreRegisterRequest thirdPartyPreRegisterRequest) {
        ApiHandler handler = handlerDispatcher
                .getHandler(thirdPartyPreRegisterRequest.getIdentifierType(), OpenApiEnum.GET_OPENID);
        if (handler == null) {
            throw new AacException(String.format("暂不支持%s获取openid", thirdPartyPreRegisterRequest.getIdentifierType()));
        }
        OpenidResponse openidResponse = handler.execute(thirdPartyPreRegisterRequest.getCodeForOpenid(), OpenidResponse.class);
        if (StrUtil.isBlank(openidResponse.getOpenid())) {
            log.error("hub获取openid失败:{}", openidResponse.getErrmsg());
            if (aacSettings.getLoginSetting().isTest()) {
                openidResponse.setOpenid(thirdPartyPreRegisterRequest.getMockOpenid());
            } else {
                throw new AacException(String.format("获取第三方openid失败:%s", openidResponse.getErrmsg()));
            }
        }
        return openidResponse;
    }

    @Override
    public WeixinAccessToken getAccessToken(GetAccessTokenRequest getAccessTokenReqeust) {
        ApiHandler handler = handlerDispatcher
                .getHandler(getAccessTokenReqeust.getIdentifierType(), OpenApiEnum.GET_ACCESS_TOKEN);
        if (handler != null) {
            return handler.execute(getAccessTokenReqeust, WeixinAccessToken.class);
        }
        throw new AacException(String.format("暂不支持%s获取access_token", getAccessTokenReqeust.getIdentifierType()));
    }
}
