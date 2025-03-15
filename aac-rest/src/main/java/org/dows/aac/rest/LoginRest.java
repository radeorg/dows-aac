package org.dows.aac.rest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.LoginApi;
import org.dows.aac.api.request.LoginRequest;
import org.dows.aac.api.response.LoginResponse;
import org.dows.aac.config.AacConfig;
import org.dows.aac.config.AacProperties;
import org.dows.framework.api.Response;
import org.dows.log.api.annotation.Actlog;
import org.dows.rbac.api.annotation.Menu;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
public class LoginRest {

    private final LoginApi loginApi;
    private final AacProperties aacProperties;
    private final AacConfig aacConfig;


    @Operation(summary = "是否开启登录")
    @GetMapping("/v1/api/aac/login/enable")
    public Boolean enableLogin(@RequestParam Boolean enable) {
        aacConfig.setLoginEnable(enable);
        return Boolean.TRUE;
    }

    /**
     * @param loginRequest
     * @param httpServletRequest
     * @return
     */
    @Actlog
    @Operation(summary = "登录")
    @PostMapping("/v1/api/aac/login")
    public Response<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        return loginApi.login(loginRequest, httpServletRequest);
    }

    /**
     * 退出登陆
     */
    @Actlog
    @Operation(summary = "登出")
    @PostMapping("/v1/api/aac/logout")
    public Response logout(HttpServletRequest request) {
        //获取token信息
        String header = request.getHeader(aacProperties.getJwtSetting().getHeader());
        log.info("header:{}", header);

        String token = header.substring(7);
        loginApi.logout(token);
        return Response.ok();
    }
}
