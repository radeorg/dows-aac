package org.dows.aac.handler.weixin;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeixinAuthUtil {
    private static final String APPID = "your_appid";
    private static final String APPSECRET = "your_appsecret";

    private static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    private static String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    public static JSONObject getAccessToken(String code) {
        try {
            URL url = new URL(ACCESS_TOKEN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return JSONUtil.parseObj(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getUserInfo(String accessToken, String openid) {
        try {
            URL url = new URL(USER_INFO_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return JSONUtil.parseObj(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}    