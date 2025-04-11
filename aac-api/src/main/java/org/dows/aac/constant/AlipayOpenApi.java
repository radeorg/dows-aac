//package org.dows.aac.api.constant;
//
//import lombok.Getter;
//
//public enum AlipayOpenApi {
//
//    /**
//     * "https://api.weixin.qq.com/sns/jscode2session" +
//     * "?appid=" + APP_ID +
//     * "&secret=" + APP_SECRET +
//     * "&js_code=" + code +
//     * "&grant_type=authorization_code";
//     */
//    WeixinJscode2Sesssion("dows.open.code", "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%$js_code=%s&grant_type=authorization_code", "JS获取微信授权登录CODE接口");
//
//
//    @Getter
//    private final String namespace;
//    @Getter
//    private final String url;
//    @Getter
//    private final String description;
//
//    AlipayOpenApi(String namespace, String url, String description) {
//        this.namespace = namespace;
//        this.url = url;
//        this.description = description;
//    }
//}
