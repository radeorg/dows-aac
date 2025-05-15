package org.dows.aac.constant;


import lombok.Getter;
import org.dows.rade.status.StatusCode;

@Getter
public enum AuthStatusCode implements StatusCode {
    UNAUTHORIZED("401", "未认证"),
    TOKEN_EXPIRED("AAC0002", "token过期"),
    FORBIDDEN("AAC0003", "被禁止");

    private final String code;
    private final String describe;

    AuthStatusCode(String code, String describe) {
        this.code = code;
        this.describe = describe;
    }

}
