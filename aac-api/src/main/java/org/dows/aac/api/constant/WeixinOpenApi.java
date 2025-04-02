//package org.dows.aac.api.constant;
//
//import lombok.Getter;
//import org.dows.aac.api.OpenApi;
//
//import java.util.Arrays;
//import java.util.List;
//
//public enum WeixinOpenApi  {
//
//    /**
//     * "https://api.weixin.qq.com/sns/jscode2session" +
//     * "?appid=" + APP_ID +
//     * "&secret=" + APP_SECRET +
//     * "&js_code=" + code +
//     * "&grant_type=authorization_code";
//     */
//    weixinJscode2Sesssion("dows.openid", "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%$js_code=%s&grant_type=authorization_code", "appid,secret,js_code", "openid", "JS获取微信授权登录CODE接口");
//
//
//    @Getter
//    private final String namespace;
//    @Getter
//    private final String url;
//    @Getter
//    private final String description;
//    @Getter
//    private final List<String> inputs;
//    @Getter
//    private final List<String> output;
//
//    WeixinOpenApi(String namespace, String url, String inputs, String output, String description) {
//        this.namespace = namespace;
//        this.url = url;
//        this.inputs = Arrays.asList(inputs.split(","));
//        this.output = Arrays.asList(output.split(","));
//        this.description = description;
//    }
//
//    @Override
//    public OpenChannel getChannel() {
//        return OpenChannel.WEIXIN;
//    }
//
//    @Override
//    public String getName() {
//        return this.name();
//    }
//
//
////    @Override
////    public <T> T execute(Object param) {
////        ApiParamHandler apiParamHandler = SpringUtil.getBean(this.name(), ApiParamHandler.class);
////        ThirdOpenApi.super.execute(param);
////    }
//}
