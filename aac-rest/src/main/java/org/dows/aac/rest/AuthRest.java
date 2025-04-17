package org.dows.aac.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.AacApi;
import org.dows.aac.api.AacUser;
import org.dows.aac.api.LoginApi;
import org.dows.aac.handler.third.ThirdPartyHandler;
import org.dows.aac.handler.uim.UimApiHandler;
import org.dows.aac.request.BindingUserRequest;
import org.dows.aac.request.LoginRequest;
import org.dows.aac.request.SyncAccountPermissionRequest;
import org.dows.aac.request.ThirdPartyPreRegisterRequest;
import org.dows.aac.response.LoginResponse;
import org.dows.aac.response.ThirdPartyAccreditResponse;
import org.dows.aac.weixin.GetTelephoneResponse;
import org.dows.aac.weixin.OpenidResponse;
import org.dows.aac.yml.AacProperties;
import org.dows.uim.response.AccountIdentifierResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
//@Namespace(module = "aac", name = "用户信息", code = "aac.info", path = "/")
@RequiredArgsConstructor
@RestController
@Tag(name="认证授权中心",description="认证授权中心")
public class AuthRest implements AacApi {

    private final LoginApi loginApi;
    private final AacProperties aacProperties;
    private final UimApiHandler uimApiHandler;
    private final ThirdPartyHandler thirdPartyHandler;

    /**
     * 是否开启登录，方便测试，生成环境切勿开启
     *
     * @param enable
     * @return
     */
    @Operation(summary = "是否开启登录")
    @GetMapping("/v1/aac/login/enable")
    public Boolean enableLogin(@RequestParam Boolean enable) {
        //aacSettings.setLoginEnable(enable);
        return Boolean.TRUE;
    }

    /**
     * 修改账号密码
     */
    //@Actlog
    @Operation(summary = "修改账号密码")
    @PostMapping("/v1/aac/account/password/update")
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


    /**
     * 第三方openid登录时的预注册，此时只创建accountIdentifier表，并不创建accountInstance表
     * @param thirdPartyPreRegisterRequest
     * @return
     */
    @PostMapping("/v1/aac/thirdparty/accredit")
    @Operation(summary = "授权获取三方信息[手机号,性别,年龄,国家...]")
    public ThirdPartyAccreditResponse preRegister(@RequestBody ThirdPartyPreRegisterRequest thirdPartyPreRegisterRequest) {
        // 获取手机号
        GetTelephoneResponse getTelephoneResponse = thirdPartyHandler
                .getThirdPartyTelephone(thirdPartyPreRegisterRequest);
        // 获取openid
        OpenidResponse thirdPartyOpenidForBindingAccount = thirdPartyHandler
                .getThirdPartyOpenid(thirdPartyPreRegisterRequest);
        ThirdPartyAccreditResponse thirdPartyAccreditResponse = new ThirdPartyAccreditResponse();
        thirdPartyAccreditResponse.setOpenid(thirdPartyOpenidForBindingAccount.getOpenid());
        thirdPartyAccreditResponse.setOpenid(thirdPartyOpenidForBindingAccount.getOpenid());
        thirdPartyAccreditResponse.setTelephone(getTelephoneResponse.getPhone_info().getPhoneNumber());
        return thirdPartyAccreditResponse;
    }


    /**
     * 第三方授权授权匹配方式登录系统
     * @param thirdPartyPreRegisterRequest
     * @return
     */
    @PostMapping("/v1/aac/bole/login")
    @Operation(summary = "伯乐小程序登录(第三方授权授权匹配模式登录系统)")
    public LoginResponse boleLogin(@RequestBody ThirdPartyPreRegisterRequest thirdPartyPreRegisterRequest,
                                   HttpServletRequest httpServletRequest) {
        // 获取openid,并新增accountIdentifier
        OpenidResponse thirdPartyOpenid = thirdPartyHandler
                .getThirdPartyOpenid(thirdPartyPreRegisterRequest);
        // 获取accountIdentifierId，即为accountIdentifier表新增openid标识，如果存在则直接返回accountIdentifierId，没有则新增
        AccountIdentifierResponse accountIdentifierResponse = uimApiHandler
                .getAccountIdentifierWithOpenid(thirdPartyOpenid, thirdPartyPreRegisterRequest);
        // 验证绑定状态，即判断是否已经绑定过手机号，没有绑定过手机号，则新增accountIdentifier表，有绑定过则直接放行
        if (accountIdentifierResponse == null) {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setState(null);
            return loginResponse;
        }
        if (accountIdentifierResponse.getAccountInstanceId() == null) {
            // 获取手机号
            GetTelephoneResponse getTelephoneResponse = thirdPartyHandler
                    .getThirdPartyTelephone(thirdPartyPreRegisterRequest);

            // 通过手机关联openid和accountInstanceId
            uimApiHandler.relevancyAccountInstanceIdForOpenid(accountIdentifierResponse,
                    getTelephoneResponse, thirdPartyPreRegisterRequest);

        }
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setIdentifierType(thirdPartyPreRegisterRequest.getIdentifierType());
        loginRequest.setIdentifier(thirdPartyOpenid.getOpenid());
        loginRequest.setOpenid(thirdPartyOpenid.getOpenid());
        // 使用openid登录
        return loginApi.login(loginRequest, httpServletRequest);
    }

    /**
     * 授权码,获取token
     *
     * @return
     */
//    @Operation(summary = "获取token")
//    @PostMapping("/v1/open/aac/token")
//    public Response getToken(@RequestBody GetTokenRequest getTokenRequest) {
//        //http://auth-server:8084/oauth2/token?redirect_uri=http://localhost:5173/user&grant_type=authorization_code&code=
//        //拼接获取token的路径
//        String url = URL.formatted(getTokenRequest.getRedirectUri(), getTokenRequest.getCode());
//        HttpUtil.post(url, "");
//        return Response.ok();
//    }
    @Operation(summary = "绑定当前账号相关信息")
    public AacUser bindingCurrentAacUser(@RequestBody BindingUserRequest bindingUserRequest) {

        //从认证信息上下文中 获取用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            throw new RuntimeException("请先登录");
        }
        // 获取手机号
//        GetTelephoneResponse getTelephoneResponse = thirdPartyUserHandler.getThirdPartyTelephoneForBindingAccount(bindingUserRequest);

        AacUser aacUser = (AacUser) authentication.getPrincipal();
        // 根据当前登录AacUser 的accountId 回填用户信息手机号，并返回用户信息
//        uimApiHandler.bindingCurrentAacUser(aacUser, getTelephoneResponse);
        //aacUser.setPhone(getTelephoneResponse.getPhone_info().getPhoneNumber());
        return aacUser;
    }

    @Operation(summary = "认领确认当前登录人信息")
    public AacUser claimCurrentAacUser(@RequestBody BindingUserRequest bindingUserRequest) {
        //从认证信息上下文中 获取用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            throw new RuntimeException("请先登录");
        }
        // 获取手机号
//        GetTelephoneResponse getTelephoneResponse = thirdPartyUserHandler.claimCurrentAacUser(bindingUserRequest);
        AacUser aacUser = (AacUser) authentication.getPrincipal();
        // 根据当前登录AacUser 的accountId 回填用户信息手机号，并返回用户信息
//        uimApiHandler.claimCurrentAacUser(aacUser, getTelephoneResponse);
        //aacUser.setPhone(getTelephoneResponse.getPhone_info().getPhoneNumber());
        return aacUser;
    }

    /**
     * 获取当前登录人信息
     *
     * @return
     */
    @Operation(summary = "获取当前登录人信息")
    public AacUser getCurrentAacUser() {
        //从认证信息上下文中 获取用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            throw new RuntimeException("请先登录");
        }
        return (AacUser) authentication.getPrincipal();
    }


    /**
     *  同步账号权限
     * @param syncAccountPermissionRequest
     */
    @Operation(summary = "同步账号权限")
    public void syncPermission(SyncAccountPermissionRequest syncAccountPermissionRequest) {

    }

    /**
     * 登录即注册，注册即登录
     *
     * @param loginRequest
     * @param httpServletRequest
     * @return
     */
    //@Actlog
    @Operation(summary = "登录")
    @PostMapping("/v1/open/aac/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        return loginApi.login(loginRequest, httpServletRequest);
    }

    /**
     * 退出登陆
     */
    //@Actlog
    @Operation(summary = "登出")
    @PostMapping("/v1/open/aac/logout")
    public void logout(HttpServletRequest request) {
        //获取token信息
        String header = request.getHeader(aacProperties.getJwtSetting().getHeader());
        log.info("header:{}", header);

        String token = header.substring(7);
        loginApi.logout(token);
    }
}
