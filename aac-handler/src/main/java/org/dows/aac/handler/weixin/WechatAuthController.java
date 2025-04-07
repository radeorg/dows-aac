//package org.dows.aac.handler;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class WechatAuthController {
//
//    @GetMapping("/callback")
//    public String handleCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
//        // 在这里可以使用获取到的 code 调用微信接口换取 access_token 和 openid
//        System.out.println("Received code: " + code);
//        System.out.println("Received state: " + state);
//        return "Callback received successfully";
//    }
//}