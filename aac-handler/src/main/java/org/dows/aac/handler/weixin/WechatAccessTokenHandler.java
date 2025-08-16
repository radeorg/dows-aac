package org.dows.aac.handler.weixin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.ApiHandler;
import org.dows.aac.api.ApiMapping;
import org.dows.aac.constant.OpenApiEnum;
import org.dows.aac.request.GetAccessTokenRequest;
import org.dows.aac.weixin.WeixinAccessToken;
import org.dows.aac.yml.OpenSetting;
import org.dows.rade.cache.RadeCache;
import org.dows.rade.constant.OpenChannel;
import org.dows.rade.context.AppContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


@RequiredArgsConstructor
@Slf4j
@Component
@ApiMapping(channel = OpenChannel.WEIXIN, func = OpenApiEnum.GET_ACCESS_TOKEN)
public class WechatAccessTokenHandler extends AbstractWeixinHandler implements ApiHandler {

    private final RadeCache radeCache;
    private static String accessToken;
    private static long expireTime;


    /*@Value("${spring.application.appId}")
    private String appId;*/

    private static final String URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    @Override
    public <T> T execute(Object inputs, Class<T> outputClass) {
        // 最好判断一下
        GetAccessTokenRequest getAccessTokenRequest = (GetAccessTokenRequest) inputs;
        // TODO
        String appId = "1";//getAccessTokenRequest.getAppId();
        if(StrUtil.isBlank(appId)){
            appId = AppContext.getAppId();
        }
        //String appId = AppContext.getAppId();
        WeixinAccessToken weixinAccessToken = radeCache.get(appId + ":wx_access_token", WeixinAccessToken.class);
        if (weixinAccessToken == null) {
            OpenSetting openSetting = verifyOpenSettingByCurrentAppId(appId);
            String uri = String.format(URL, openSetting.getThirdAppId(), openSetting.getSecret());
            String response = HttpUtil.get(uri);
            // todo 转为对应的对象处理
            weixinAccessToken = (WeixinAccessToken) JSONUtil.toBean(response, outputClass);
            radeCache.set(appId + ":access_token", weixinAccessToken, (long) weixinAccessToken.getExpires_in());
        }
        return (T) weixinAccessToken;
    }


    private static String getAccessToken() {
        try {
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = JSONUtil.parseObj(response.toString());
                if (jsonObject.containsKey("access_token")) {
                    return jsonObject.getStr("access_token");
                } else {
                    System.err.println("Failed to get access token: " + jsonObject.getStr("errmsg"));
                }
            } else {
                System.err.println("HTTP request failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}