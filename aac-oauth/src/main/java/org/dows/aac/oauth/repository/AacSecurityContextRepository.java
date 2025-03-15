package org.dows.aac.oauth.repository;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.AuthKey;
import org.dows.aac.oauth.SupplierDeferredSecurityContext;
import org.dows.framework.cache.caffeine.CaffeineTemplate;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.function.Supplier;

/**
 * 基于caffeine存储认证信息
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AacSecurityContextRepository implements SecurityContextRepository {



    /**
     * 上下文持有人策略
     */
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();


    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        // 方法已过时，使用 loadDeferredContext 方法
        throw new UnsupportedOperationException("方法已过时");
    }

    /**
     * 保存上下文
     *
     * @param context  the non-null context which was obtained from the holder.
     * @param request
     * @param response
     */
    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        String rzId = getRzId(request);
        if (ObjectUtils.isEmpty(rzId)) {
            return;
        }
        String key = AuthKey.CACHE_KEY_PREFIX.buildKey(rzId);
        // 如果当前的context是空的，则移除
        SecurityContext emptyContext = this.securityContextHolderStrategy.createEmptyContext();
//        if (emptyContext.equals(context)) {
//            redisTemplate.delete(key);
//        } else {
//            // 保存认证信息 过期时间1个小时 保持和access_token的过期时间一致
//            redisTemplate.opsForValue().set(key, context, 1, TimeUnit.HOURS);
//        }
    }

    /**
     * 判断是否存在上下文
     *
     * @param request the current request
     * @return
     */
    @Override
    public boolean containsContext(HttpServletRequest request) {
        String rzId = getRzId(request);
        if (ObjectUtils.isEmpty(rzId)) {
            return false;
        }
        String key = AuthKey.CACHE_KEY_PREFIX.buildKey(rzId);
        //return redisTemplate.opsForValue().get(key) != null;
        return false;
    }

    /**
     * 加载上下文
     *
     * @param request the {@link HttpServletRequest} to load the {@link SecurityContext}
     *                from
     * @return
     */
    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
        //从redis中读取上下文
        Supplier<SecurityContext> supplier = () -> getContextByRedis(request);
        return new SupplierDeferredSecurityContext(supplier, this.securityContextHolderStrategy);
    }

    /**
     * 从redis中获取认证信息
     *
     * @param request 当前请求
     * @return 认证信息
     */
    private SecurityContext getContextByRedis(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String rzId = getRzId(request);
        if (ObjectUtils.isEmpty(rzId)) {
            return null;
        }
        String key = AuthKey.CACHE_KEY_PREFIX.buildKey(rzId);
        // 根据缓存 获取认证信息
//        Object o = redisTemplate.opsForValue().get(key);
//        //直接返回上下文 就不需要再登陆了 否则会跳转到登陆界面
//        return (SecurityContext) o;
        return null;
    }


    /**
     * 获取认证Id
     *
     * @param request
     * @return
     */
    private String getRzId(HttpServletRequest request) {
        //从header头中获取认证id
        String rzId = request.getHeader("tid");
        if (StrUtil.isEmpty(rzId)) {
            //如果header头是空的 那么在从参数获取认证id
            rzId = request.getParameter("tid");
        }
        return rzId;
    }

}

