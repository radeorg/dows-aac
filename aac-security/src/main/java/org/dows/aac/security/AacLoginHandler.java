package org.dows.aac.security;

import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.AacException;
import org.dows.aac.api.AacUser;
import org.dows.aac.api.LoginApi;
import org.dows.aac.api.constant.AuthKey;
import org.dows.aac.api.request.LoginRequest;
import org.dows.aac.api.response.LoginResponse;
import org.dows.aac.yml.AacProperties;
import org.dows.rade.cache.RadeCache;
import org.dows.rbac.api.constant.CacheKeyEnum;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AacLoginHandler implements LoginApi {

    //认证管理器
    private final AuthenticationManager authenticationManager;

    private final RadeCache radeCache;
//    private final AacSettings aacSettings;

    private final AacProperties aacProperties;

    /**
     * 登陆
     *
     * @param loginRequest
     * @param request
     * @return LoginResponse
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request) {
        //根据账号和密码 创建 认证令牌对象
        UsernamePasswordAuthenticationToken upt =
                new UsernamePasswordAuthenticationToken(loginRequest.getIdentifier(), loginRequest.getPassword());
        String appId = request.getHeader("AppId");
        if (appId != null && appId.isBlank()) {
            throw new AacException("appId不能为空");
        }
        loginRequest.setAppId(appId);
        upt.setDetails(loginRequest);
        //进行登录 获取认证信息
        Authentication authenticate = authenticationManager.authenticate(upt);
        if (authenticate == null) {
            throw new UsernameNotFoundException("登录失败");
        }
        if (aacProperties.getLoginSetting().getType().equalsIgnoreCase("single")) {
            return singleToken(loginRequest, authenticate);
        }
        if (aacProperties.getLoginSetting().getType().equalsIgnoreCase("oauth")) {
            return oauthToken(loginRequest, authenticate);
        }
        throw new AuthenticationServiceException("登录失败");
    }


    /**
     * 单体登录
     *
     * @param loginRequest
     * @return
     */
    public LoginResponse singleToken(LoginRequest loginRequest, Authentication authenticate) {
        //创建安全上下文
        SecurityContext securityContext = SecurityContextHolder.getContext();
        //把用户认证信息放到 安全上下文中
        securityContext.setAuthentication(authenticate);
        //把上下文放到 持有人手中
        SecurityContextHolder.setContext(securityContext);
        AacUser aacUser = (AacUser) authenticate.getPrincipal();
        if (null == aacUser) {
            throw new UsernameNotFoundException("not exits account!");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("accountId", aacUser.getAccountId().toString());
        map.put("roleIds", aacUser.getRoleIds());
        map.put("superAccount", aacUser.isSuperAccount());
        String token = JWTUtil.createToken(map,
                aacProperties.getJwtSetting().getSecretKey().getBytes(StandardCharsets.UTF_8));
        // 缓存
        radeCache.set(CacheKeyEnum.SECURITY_CONTEXT.getCacheKey(token), securityContext);
        log.info("account:{},token:{}", loginRequest.getIdentifier(), token);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return loginResponse;
    }

    /**
     * 授权登录
     *
     * @param loginRequest
     * @return
     */
    public LoginResponse oauthToken(LoginRequest loginRequest, Authentication authenticate) {
        //认证Id
        String id = UUID.randomUUID().toString();
        String key = AuthKey.CACHE_KEY_PREFIX.buildKey(id);
        //创建安全上下文
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        //把用户认证信息放到 安全上下文中
        securityContext.setAuthentication(authenticate);
        //把上下文放到 持有人手中
        SecurityContextHolder.setContext(securityContext);

        AacUser aacUser = (AacUser) authenticate.getPrincipal();
        if (null == aacUser) {
            throw new UsernameNotFoundException("登录失败");
        }
        Map<String, String> map = new HashMap<>();
        map.put("accountId", aacUser.getAccountId().toString());
        String token = null;//JWTUtil.getToken(map, aacProperties.getJwtSetting().getSecretKey());
        //radeCache.putCache(UserInfoEnum.SECURITY_CONTEXT.getKey(), token, securityContext);
        log.info("账号： " + loginRequest.getIdentifier() + "token: " + token);

        // 保存认证信息 过期时间1个小时 保持和access_token的过期时间一致
        //redisTemplate.opsForValue().set(key, securityContext, 1, TimeUnit.HOURS);
        //radeCache.putCache("aac", key, securityContext);

        LoginResponse response = new LoginResponse();
        response.setToken(token);

        return response;
    }

    /**
     * 退出
     *
     * @param token
     */
    @Override
    public void logout(String token) {
//        String key = AuthKey.CACHE_KEY_PREFIX.buildKey(token);
        //清空上下文
        SecurityContextHolder.clearContext();
        radeCache.del(CacheKeyEnum.SECURITY_CONTEXT.getCacheKey(token));
        //删除缓存
//        redisTemplate.delete(key);
    }
}
