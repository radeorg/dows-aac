package org.dows.aac.rest.biz;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.LoginApi;
import org.dows.aac.handler.third.ThirdPartyHandler;
import org.dows.aac.handler.uim.UimApiHandler;
import org.dows.aac.request.LoginRequest;
import org.dows.aac.request.ThirdPartyPreRegisterRequest;
import org.dows.aac.response.LoginResponse;
import org.dows.aac.weixin.GetTelephoneResponse;
import org.dows.aac.weixin.OpenidResponse;
import org.dows.uim.response.AccountIdentifierResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author tangsm
 * @data 2025/5/23 星期五
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BoleBiz {
    private final LoginApi loginApi;
    private final UimApiHandler uimApiHandler;
    private final ThirdPartyHandler thirdPartyHandler;

    /**
     * 伯乐小程序登录(第三方授权授权匹配模式登录系统)
     * @return LoginResponse
     */
    @Transactional
    public LoginResponse boleLogin(ThirdPartyPreRegisterRequest thirdPartyPreRegisterRequest,
                                   HttpServletRequest httpServletRequest){
        // 获取openid,并新增accountIdentifier
        OpenidResponse thirdPartyOpenid = thirdPartyHandler
                .getThirdPartyOpenid(thirdPartyPreRegisterRequest);
        // 获取accountIdentifierId，即为accountIdentifier表新增openid标识，如果存在则直接返回accountIdentifierId，没有则新增
        AccountIdentifierResponse accountIdentifierResponse = uimApiHandler
                .getAccountIdentifierWithOpenid(thirdPartyOpenid, thirdPartyPreRegisterRequest);
        // 验证绑定状态，即判断是否已经绑定过手机号，没有绑定过手机号，则新增accountIdentifier表，有绑定过则直接放行
        if (accountIdentifierResponse == null) {
            LoginResponse loginResponse = new LoginResponse();
            //loginResponse.setAccountIdentifierId(accountIdentifierResponse.getAccountIdentifierId());
            loginResponse.setState(null);
            return loginResponse;
        }
        if (accountIdentifierResponse.getAccountInstanceId() == null) {
            if(thirdPartyPreRegisterRequest.getCodeForData() != null) {
                // 获取手机号
                GetTelephoneResponse getTelephoneResponse = thirdPartyHandler
                        .getThirdPartyTelephone(thirdPartyPreRegisterRequest);

                // 通过手机关联openid和accountInstanceId
                uimApiHandler.relevancyAccountInstanceIdForOpenid(accountIdentifierResponse,
                        getTelephoneResponse, thirdPartyPreRegisterRequest);
            } else {
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setAccountIdentifierId(accountIdentifierResponse.getAccountIdentifierId());
                loginResponse.setState(null);
                return loginResponse;
            }
        }
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setIdentifierType(thirdPartyPreRegisterRequest.getIdentifierType());
        loginRequest.setIdentifier(thirdPartyOpenid.getOpenid());
        loginRequest.setOpenid(thirdPartyOpenid.getOpenid());
        // 使用openid登录
        return loginApi.login(loginRequest, httpServletRequest);
    }
}
