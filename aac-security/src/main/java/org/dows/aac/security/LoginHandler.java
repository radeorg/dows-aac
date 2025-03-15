package org.dows.aac.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.AacUser;
import org.dows.aac.api.AuthKey;
import org.dows.aac.api.LoginApi;
import org.dows.aac.api.request.LoginRequest;
import org.dows.aac.api.utils.JWTUtil;
import org.dows.aac.config.AacProperties;
import org.dows.aac.handler.AacCache;
import org.dows.framework.api.Response;
import org.dows.rbac.api.constant.UserInfoEnum;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginHandler implements LoginApi {

    //认证管理器
    private final AuthenticationManager authenticationManager;

    private final AacCache aacCache;

    private final AacProperties aacProperties;

    /**
     * 登陆
     *
     * @param loginRequest
     * @param request
     * @return
     */
    @Override
    public Response login(LoginRequest loginRequest, HttpServletRequest request) {
        //根据账号和密码 创建 认证令牌对象
        UsernamePasswordAuthenticationToken upt =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        //进行登录 获取认证信息
        Authentication authenticate = authenticationManager.authenticate(upt);
        if (authenticate == null) {
            throw new UsernameNotFoundException("登录失败");
        }
        if (aacProperties.getLogin().getType().equalsIgnoreCase("single")) {
            return singleToken(loginRequest, authenticate);
        }
        if (aacProperties.getLogin().getType().equalsIgnoreCase("oauth")) {
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
    public Response singleToken(LoginRequest loginRequest, Authentication authenticate) {
        //创建安全上下文
        SecurityContext securityContext = SecurityContextHolder.getContext();
        //把用户认证信息放到 安全上下文中
        securityContext.setAuthentication(authenticate);
        //把上下文放到 持有人手中
        SecurityContextHolder.setContext(securityContext);
        AacUser aacUser = (AacUser)authenticate.getPrincipal();
        if(null == aacUser){
            throw new UsernameNotFoundException("登录失败");
        }
//        aacCache.putCache(UserInfoEnum.USER_INFO.getKey(),aacUser.getAccountId().toString(),aacUser);
        Map<String, String> map = new HashMap<>();
        map.put("accountId", aacUser.getAccountId().toString());
        String token =  JWTUtil.getToken(map, aacProperties.getJwtSetting().getSecretKey());
        aacCache.putCache(UserInfoEnum.SECURITY_CONTEXT.getKey(), token,securityContext);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        //第二个参数是盐值
        log.info("账号： " + loginRequest.getUsername() + "token: "+token);
        return Response.ok(result);
    }

    /**
     * 授权登录
     *
     * @param loginRequest
     * @return
     */
    public Response oauthToken(LoginRequest loginRequest, Authentication authenticate) {
        //认证Id
        String id = UUID.randomUUID().toString();
        String key = AuthKey.CACHE_KEY_PREFIX.buildKey(id);
        //创建安全上下文
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        //把用户认证信息放到 安全上下文中
        securityContext.setAuthentication(authenticate);
        //把上下文放到 持有人手中
        SecurityContextHolder.setContext(securityContext);

        // 保存认证信息 过期时间1个小时 保持和access_token的过期时间一致
        //redisTemplate.opsForValue().set(key, securityContext, 1, TimeUnit.HOURS);
        aacCache.putCache("aac", key, securityContext);
        return Response.ok(id);
    }

    /**
     * 退出
     *
     * @param id
     */
    @Override
    public void logout(String token) {
//        String key = AuthKey.CACHE_KEY_PREFIX.buildKey(token);
        //清空上下文
        SecurityContextHolder.clearContext();
        aacCache.evictCache(UserInfoEnum.SECURITY_CONTEXT.getKey(), token);
        //删除缓存
//        redisTemplate.delete(key);
    }
}
