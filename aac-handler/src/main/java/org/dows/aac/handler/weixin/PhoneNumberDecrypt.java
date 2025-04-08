package org.dows.aac.handler.weixin;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PhoneNumberDecrypt {
    public static String decryptPhoneNumber(String sessionKey, String encryptedData, String iv) {
        try {
            byte[] sessionKeyByte = Base64.getDecoder().decode(sessionKey);
            byte[] encryptedDataByte = Base64.getDecoder().decode(encryptedData);
            byte[] ivByte = Base64.getDecoder().decode(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(sessionKeyByte, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] resultByte = cipher.doFinal(encryptedDataByte);
            if (resultByte != null && resultByte.length > 0) {
                String result = new String(resultByte, StandardCharsets.UTF_8);
                // 解析解密后的JSON字符串，获取手机号相关信息
                // 假设解密后是标准JSON格式，包含phoneNumber字段
                // 这里可使用JSON解析库（如Jackson、Gson等）进行解析
                // 简单示例，仅为说明获取手机号的逻辑
                int startIndex = result.indexOf("phoneNumber\":\"") + "phoneNumber\":\"".length();
                int endIndex = result.indexOf("\"", startIndex);
                return result.substring(startIndex, endIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}