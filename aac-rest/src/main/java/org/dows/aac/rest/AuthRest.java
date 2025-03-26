package org.dows.aac.rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.AacSettings;
import org.dows.aac.api.AacUser;
import org.dows.aac.api.LoginApi;
import org.dows.aac.api.request.LoginRequest;
import org.dows.aac.api.response.LoginResponse;
import org.dows.aac.yml.AacProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
//@Namespace(module = "aac", name = "用户信息", code = "aac.info", path = "/")
@RequiredArgsConstructor
@RestController
public class AuthRest {

    private final LoginApi loginApi;
    private final AacProperties aacProperties;
    private final AacSettings aacSettings;


    @Operation(summary = "是否开启登录")
    @GetMapping("/v1/api/aac/login/enable")
    public Boolean enableLogin(@RequestParam Boolean enable) {
        //aacSettings.setLoginEnable(enable);
        return Boolean.TRUE;
    }

    /**
     * @param loginRequest
     * @param httpServletRequest
     * @return
     */
    //@Actlog
    @Operation(summary = "登录")
    @PostMapping("/v1/api/aac/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        return loginApi.login(loginRequest, httpServletRequest);
    }

    /**
     * 退出登陆
     */
    //@Actlog
    @Operation(summary = "登出")
    @PostMapping("/v1/api/aac/logout")
    public void logout(HttpServletRequest request) {
        //获取token信息
        String header = request.getHeader(aacProperties.getJwtSetting().getHeader());
        log.info("header:{}", header);

        String token = header.substring(7);
        loginApi.logout(token);
    }
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
    public AacUser getUserInfo() {
        //从认证信息上下文中 获取用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            throw new RuntimeException("请先登录");
        }
        AacUser aacUser = (AacUser) authentication.getPrincipal();
        return aacUser;
    }


    /**
     * 修改账号密码
     */
    //@Actlog
    @Operation(summary = "修改账号密码")
    @PostMapping("/v1/api/aac/updatePassword")
    public Boolean updatePassword(String oldPassword, String newPassword) {
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
        return true;
    }

}
