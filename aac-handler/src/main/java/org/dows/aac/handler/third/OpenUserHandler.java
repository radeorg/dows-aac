package org.dows.aac.handler.third;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.AacSettings;
import org.dows.aac.api.ApiHandler;
import org.dows.aac.constant.OpenApiEnum;
import org.dows.aac.exception.AacException;
import org.dows.aac.handler.HandlerDispatcher;
import org.dows.aac.request.BindingUserRequest;
import org.dows.aac.weixin.GetTelephoneRequest;
import org.dows.aac.weixin.GetTelephoneResponse;
import org.dows.aac.weixin.WeixinAccessToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenUserHandler {
    private final AacSettings aacSettings;
    private final HandlerDispatcher handlerDispatcher;

    public GetTelephoneResponse bindingCurrentAacUser(BindingUserRequest bindingUserRequest) {
        // 获取access_token
        ApiHandler tokenHandler = handlerDispatcher
                .getHandler(bindingUserRequest.getIdentifierType(), OpenApiEnum.GET_ACCESS_TOKEN);
        WeixinAccessToken weixinAccessToken = tokenHandler.execute(null, WeixinAccessToken.class);
        if (weixinAccessToken.getErrcode() != null) {
            throw new AacException(weixinAccessToken.getErrmsg());
        }
        // 根据access_token 获取手机号
        ApiHandler handler = handlerDispatcher
                .getHandler(bindingUserRequest.getIdentifierType(), OpenApiEnum.GET_TELEPHONE);
        GetTelephoneRequest getTelephoneRequest = new GetTelephoneRequest();
        getTelephoneRequest.setCode(bindingUserRequest.getEncryptIdentifier());
        getTelephoneRequest.setAccess_token(weixinAccessToken.getAccess_token());
        GetTelephoneResponse getTelephoneResponse = handler.execute(getTelephoneRequest, GetTelephoneResponse.class);
        if (getTelephoneResponse.getErrcode() != null) {
            if (!this.aacSettings.getLoginSetting().isTest()) {
                throw new AacException(String.format("获取微信信息绑定账号失败,错误码:%s,错误信息:%s",
                        getTelephoneResponse.getErrcode(), getTelephoneResponse.getErrmsg()));
            }
            GetTelephoneResponse.PhoneInfo phoneInfo = new GetTelephoneResponse.PhoneInfo();
            String telephone = bindingUserRequest.getTelephone();
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

    public GetTelephoneResponse claimCurrentAacUser(BindingUserRequest bindingUserRequest) {

        return null;
    }
}
