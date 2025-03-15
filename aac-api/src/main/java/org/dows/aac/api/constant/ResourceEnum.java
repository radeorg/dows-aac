package org.dows.aac.api.constant;

public enum ResourceEnum {
    INTERFACE(0, "接口"),
    MENU(1, "菜单");

    private final int code;
    private final String description;

    ResourceEnum(int code, String description) {
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
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Type code: " + code);
    }
}