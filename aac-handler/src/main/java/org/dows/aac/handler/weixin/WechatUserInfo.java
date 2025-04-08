package org.dows.aac.handler.weixin;

import lombok.Data;

@Data
public class WechatUserInfo {
    private String openId;
    private String nickName;
    private int gender;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;
    private String unionId;
    private Watermark watermark;

    @Data
    public static class Watermark {
        private String appid;
        private long timestamp;

    }
}    