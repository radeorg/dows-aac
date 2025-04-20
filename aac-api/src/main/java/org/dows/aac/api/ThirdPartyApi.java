package org.dows.aac.api;

import org.dows.aac.exception.AacException;
import org.dows.aac.request.GetAccessTokenRequest;
import org.dows.aac.request.ThirdPartyPreRegisterRequest;
import org.dows.aac.weixin.GetTelephoneResponse;
import org.dows.aac.weixin.OpenidResponse;
import org.dows.aac.weixin.WeixinAccessToken;

public interface ThirdPartyApi {


    /**
     * 获取第三方用户的手机号
     *
     * @param thirdPartyPreRegisterRequest
     * @return
     */
    default GetTelephoneResponse getThirdPartyTelephone(ThirdPartyPreRegisterRequest thirdPartyPreRegisterRequest) {
        throw new AacException("暂不支持获取手机号");
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
    default OpenidResponse getThirdPartyOpenid(ThirdPartyPreRegisterRequest thirdPartyPreRegisterRequest) {
        throw new AacException("暂不支持获取openid");
    }


    /**
     * 获取第三方accessToken
     * 微信：https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
     * 支付宝：
     * 抖音：
     *
     * @param getAccessTokenRequest
     * @return
     */
    default WeixinAccessToken getAccessToken(GetAccessTokenRequest getAccessTokenRequest) {
        throw new AacException("暂不支持获取accessToken");
    }
}
