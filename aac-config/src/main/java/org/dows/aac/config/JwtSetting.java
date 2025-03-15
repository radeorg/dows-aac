package org.dows.aac.config;

import lombok.Data;

/**
 * @description: header: Authorization
 * # 令牌密钥
 * secret-key: Yx7GcP3UY194v8U.fLyhBiFZFxKOagQZt1baEhKlTfMW
 * # 令牌有效期
 * expiration: 86400000 # a day
 * refresh-expiration: 604800000 # 7 days</br>
 * @author: lait.zhang@gmail.com
 * @date: 3/18/2024 11:46 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class JwtSetting {
    // 令牌请求头
    private String header;
    // 令牌密钥
    private String secretKey;
    // token的过期时间
    private String expiration;
    // 刷新令牌的过期时间
    private String refreshExpiration;
}

