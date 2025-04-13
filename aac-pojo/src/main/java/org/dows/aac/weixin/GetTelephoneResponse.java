package org.dows.aac.weixin;

import lombok.Data;

@Data
public class GetTelephoneResponse {
    //	number	错误码
    private String errcode;
    //string	错误信息
    private String errmsg;
    //object	用户手机号信息
    private PhoneInfo phone_info;

    @Data
    public static class PhoneInfo {
        //string	用户绑定的手机号（国外手机号会有区号）
        private String phoneNumber;
        //string	没有区号的手机号
        private String purePhoneNumber;
        //	string	区号
        private String countryCode;
    }



}
