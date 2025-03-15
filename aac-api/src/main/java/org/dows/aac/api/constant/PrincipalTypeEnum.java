package org.dows.aac.api.constant;

public enum PrincipalTypeEnum {
    PERSONAL(0, "个人"),
    ORGANIZATION(1, "组织");

    private final int code;
    private final String description;

    PrincipalTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PrincipalTypeEnum getByCode(int code) {
        for (PrincipalTypeEnum type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid PrincipalType code: " + code);
    }
}