package org.dows.aac.api.request;

import lombok.Data;

@Data
public class Oauth2Request {

    //回调地址
    private String redirectUri;

    //授权码
    private String code;

    //客户端id
    private String clientId;

    //客户端secret
    private String clientSecret;

}
