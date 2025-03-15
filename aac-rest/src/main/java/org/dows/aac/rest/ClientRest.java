//package org.dows.aac.rest;
//
//import lombok.extern.slf4j.Slf4j;
//import org.dows.aac.api.request.RegisterClientRequest;
//import org.dows.framework.api.Response;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.core.oidc.OidcScopes;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.Duration;
//import java.util.UUID;
//
//@Slf4j
//@RestController
//public class ClientRest {
//
//    @Autowired
//    private RegisteredClientRepository registeredClientRepository;
//
////    /**
////     *
////     * 获取授权码
////     * @param
////     * @return
////     * @throws Exception
////     */
////    @GetMapping("/authorized")
////    public String authorized(String code){
////        log.info("授权码是:{}",code);
////        return code;
////    }
//
//    /**
//     * 注册客户端
//     *
//     * @return
//     */
//    @PostMapping("/clinet/register")
//    public Response registerClientDianShang(RegisterClientRequest registerClientRequest) {
//        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                //客户端id
//                .clientId("shdy")
//                //如果不写,就会生成一个随机的uuid
//                .clientName("第因")
//                //客户端密码 加密
//                .clientSecret(new BCryptPasswordEncoder().encode("123456"))
//                //客户端认证方法  客户端安全基本 客户端通过客户端密码认证方式接入
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                //客户端允许使用的授权模式,授权类型,授权码模式,刷新token,客户端模式
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                //客户端允许跳转的uri注册地址,回调地址 登陆成功之后跳转的地址
//                .redirectUri("http://localhost:3001/callback")
//                //客户端允许使用的范围授权 如果对方写的不对 那么就没有权限进行登陆
//                .scope(OidcScopes.OPENID)
//                .tokenSettings(TokenSettings.builder()
//                        //accessToken 生存时间 1个小时
//                        .accessTokenTimeToLive(Duration.ofSeconds(3600))
//                        //刷新token 生存时间  1个小时过期后再次请求授权 在给accessToken续上1个小时
//                        .refreshTokenTimeToLive(Duration.ofSeconds(3600))
//                        .build())
//                .build();
//        //保存到oauth2_registered_client表里
//        registeredClientRepository.save(registeredClient);
//        return Response.ok();
//    }
//
////    public static void main(String[] args) {
////        System.out.println(new BCryptPasswordEncoder().encode("123456"));
////    }
//
//}