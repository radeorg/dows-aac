package org.dows.aac.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class LoginRequest {
    // 标识类型[账号，手机，邮箱]
    private int identifierType;
    //账号标识[账号，手机，邮箱]
    private String identifier;
    //密码
    private String password;
    // 验证码p[短信，邮箱]
    private String verifyCode;
    private String source;
    private String zoneNo;
    // 推荐码
    private String referralsNo;
    private String avator;
    // 应用ID
    @JsonIgnore
    private String appId;

}
