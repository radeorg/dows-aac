package org.dows.aac.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.dows.aac.api.AacUser;
import org.dows.rade.web.Response;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//@Namespace(module = "aac", name = "用户信息", code = "aac.info", path = "/")
@RequiredArgsConstructor
@RestController
public class AuthRest {
    private final String URL = "http://auth-server:8084/oauth2/token?redirect_uri=%s&grant_type=authorization_code&code=%s";
//    private final RbacApi rbacApi;
//    private final AccountApi accountApi;

    /**
     * 授权码,获取token
     *
     * @return
     */
//    @Operation(summary = "获取token")
//    @PostMapping("/v1/api/aac/getToken")
//    public Response getToken(@RequestBody GetTokenRequest getTokenRequest) {
//        //http://auth-server:8084/oauth2/token?redirect_uri=http://localhost:5173/user&grant_type=authorization_code&code=
//        //拼接获取token的路径
//        String url = URL.formatted(getTokenRequest.getRedirectUri(), getTokenRequest.getCode());
//        HttpUtil.post(url, "");
//        return Response.ok();
//    }

    /**
     * 获取当前登录人信息
     *
     * @return
     */
    //@Actlog
    @Operation(summary = "获取当前登录人信息")
    @GetMapping("/v1/api/aac/getCurrentUser")
    public Response getUserInfo() {
        //从认证信息上下文中 获取用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            throw new RuntimeException("请先登录");
        }
        AacUser aacUser = (AacUser) authentication.getPrincipal();
        return Response.ok(aacUser);
    }


    /**
     * 修改账号密码
     */
    //@Actlog
    @Operation(summary = "修改账号密码")
    @PostMapping("/v1/api/aac/updatePassword")
    public Response updatePassword(String oldPassword, String newPassword) {
        // 验证原密码的正确性
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
//        if (principal instanceof String) {
//            throw new CredentialsExpiredException(AuthStatusCode.UNAUTHORIZED.getDescr());
//        }
//        AacUser aacUser = (AacUser) principal;
//        AccountInstanceResponse accountInstance = accountApi.getAccountInstanceById(aacUser.getAccountId());
//        BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
//        if (!encode.matches(oldPassword, accountInstance.getPassword())) {
//            return Response.fail("原密码不正确");
//        }
//        accountApi.updateInstancePassword(accountInstance.getAccountInstanceId(), new BCryptPasswordEncoder().encode(newPassword));
        return Response.ok();
    }

}
