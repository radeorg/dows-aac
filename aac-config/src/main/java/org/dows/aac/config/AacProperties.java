package org.dows.aac.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 3/13/2024 3:23 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
@ConfigurationProperties(prefix = "dows.aac")
public class AacProperties {

    private Login login;
    private String[] whitelist;
    private JwtSetting jwtSetting;

    @Data
    public static class Login {
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

    @Data
    public static class JwtSetting {
        // 令牌请求头
        private String header;
        // 令牌密钥
        private String secretKey;
        // token的过期时间
        private String expiration;
        // 刷新令牌的过期时间
        private String refreshExpiration;
    }

}

