package org.dows.aac.api.constant;

public enum UserInfoEnum {
    SECURITY_CONTEXT(0, "111");
    private final int code;
    private final String key;

    UserInfoEnum(int code, String key) {
        this.code = code;
        this.key = key;
    }

    public int getCode() {
        return code;
    }

    public String getKey() {
        return key;
    }

    public static ResourceEnum getByCode(int code) {
        for (ResourceEnum type : ResourceEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Type code: " + code);
    }
}
