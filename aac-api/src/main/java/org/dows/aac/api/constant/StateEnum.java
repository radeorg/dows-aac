package org.dows.aac.api.constant;

public enum StateEnum {
    AVAILABLE(0, "可用"),
    NOAVAILABLE(1, "不可用");

    private final int code;
    private final String description;

    StateEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
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
