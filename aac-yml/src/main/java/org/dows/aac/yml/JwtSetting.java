package org.dows.aac.yml;

import lombok.Data;

@Data
    public  class JwtSetting {
        // 令牌请求头
        private String header;
        // 令牌密钥
        private String secretKey;
        // token的过期时间
        private String expiration;
        // 刷新令牌的过期时间
        private String refreshExpiration;
    }