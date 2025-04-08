package org.dows.aac.handler.weixin;

import cn.hutool.json.JSONUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class WXBizDataCrypt {
    private String appId;
    private String sessionKey;

    public WXBizDataCrypt(String appId, String sessionKey) {
        this.appId = appId;
        this.sessionKey = sessionKey;
    }

    public WechatUserInfo decrypt(String encryptedData, String iv) throws Exception {
        // base64 decode
        byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);
        byte[] ivBytes = Base64.getDecoder().decode(iv);

        // 创建 AES 解密器
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(sessionKeyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // 解密数据
        byte[] decryptedBytes = cipher.doFinal(encryptedDataBytes);
        String decryptedStr = new String(decryptedBytes, StandardCharsets.UTF_8);

        // 解析 JSON 数据
        WechatUserInfo wechatUserInfo = JSONUtil.toBean(decryptedStr, WechatUserInfo.class);
        //Map<String, Object> decrypted = (Map<String, Object>) bean;

        // 验证 appid
        WechatUserInfo.Watermark watermark = wechatUserInfo.getWatermark();
        if (watermark == null || !appId.equals(watermark.getAppid())) {
            throw new Exception("Invalid Buffer");
        }

        return wechatUserInfo;
    }

    public static void main(String[] args) {
        String appId = "your_app_id";
        String sessionKey = "your_session_key";
        String encryptedData = "your_encrypted_data";
        String iv = "your_iv";

        try {
            WXBizDataCrypt decryptor = new WXBizDataCrypt(appId, sessionKey);
            WechatUserInfo result = decryptor.decrypt(encryptedData, iv);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}    