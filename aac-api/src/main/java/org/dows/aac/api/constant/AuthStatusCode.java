package org.dows.aac.api.constant;


public enum AuthStatusCode  {
    UNAUTHORIZED("AAC0001", "未认证"),
    TOKEN_EXPIRED("AAC0002", "token过期"),
    FORBIDDEN("AAC0003", "被禁止");

    private final String code;
    private final String describe;

    AuthStatusCode(String code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescribe() {
        return this.describe;
    }
}
