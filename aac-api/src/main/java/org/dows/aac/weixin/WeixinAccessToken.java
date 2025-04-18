package org.dows.aac.weixin;

import lombok.Data;

@Data
public class WeixinAccessToken {
    //string	获取到的凭证
    private String access_token;
    //number	凭证有效时间，单位：秒。目前是 7200 秒之内的值。
    private int expires_in;
    //错误码
    private String errcode;
    //错误信息
    private String errmsg;

}
