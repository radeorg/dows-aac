package org.dows.aac.weixin;

import lombok.Data;

@Data
public class GetTelephoneRequest {

    private String code;
    private String access_token;
    private String openid;
}
