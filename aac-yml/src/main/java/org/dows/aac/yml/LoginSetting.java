package org.dows.aac.yml;

import lombok.Data;

@Data
public class LoginSetting {
    // 是否启用登录
    private boolean enabled;
    // 登录类型
    private String type;


    private String loginUrl;
    private String logoutUrl;
    private String logoutType;
    private String logoutRedirectUrl;
    private String logoutSuccessUrl;
    private String logoutFailureUrl;
    private String logoutFailureRedirectUrl;

}