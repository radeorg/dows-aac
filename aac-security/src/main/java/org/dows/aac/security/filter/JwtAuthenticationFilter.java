package org.dows.aac.security.filter;

import cn.hutool.jwt.JWTUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.security.AacCache;
import org.dows.aac.security.UserDetailsServiceHandler;
import org.dows.aac.yml.AacConfig;
import org.dows.aac.yml.AacProperties;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 只登陆1次
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceHandler userDetailsServiceHandler;

    private final AacProperties aacProperties;


    private final AacConfig aacConfig;

    private final AacCache aacCache;

    List<String> whiteList;

    @PostConstruct
    public void init() {
        whiteList = Arrays.stream(aacConfig.getWhitelist()).toList();
    }

    /**
     * 所有请求的过滤器
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (whiteList.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        //origin：指定可以访问本项目的IP
        String origin = request.getHeader("Origin");
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "1800");
        //  设置  受支持请求标头（自定义  可以访问的请求头  例如：Token）
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization,token,Origin,Content-Type,Accept");
        // 指示的请求的响应是否可以暴露于该页面。当true值返回时它可以被暴露
        response.setHeader("Access-Control-Allow-Credentials", "true");
        //如果是OPTIONS请求，让其响应一个 200状态码，说明可以正常访问
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        //获取token信息
        String header = request.getHeader(aacProperties.getJwtSetting().getHeader());
        log.info("header:{}", header);
        //注意Bearer后面还有一个空格
        if (!StringUtils.hasLength(header) || !StringUtils.startsWithIgnoreCase(header, "Bearer ")) {
            // 不需要登录验证
            if (!aacConfig.isEnableLogin()) {
                filterChain.doFilter(request, response);
                return;
            }
            //如果请求头是空的 或者 前置没有以Bearer 开头 那么进入下一个过滤器链
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.substring(7);
        JWTUtil.verify(token, aacProperties.getJwtSetting().getSecretKey());

        Object o = aacCache.getCacheValue(UserInfoEnum.SECURITY_CONTEXT.getKey(), token);

        if (o == null) {
            //如果缓存没有 那么放行
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContext securityContext = (SecurityContext) o;
        //把上下文信息放入持有人手中 这样别的请求在进来 就有认证的权限了 就不需要再登陆了
        SecurityContextHolder.setContext(securityContext);
        //获取认证信息是否存在
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (!StringUtils.isEmpty(accountName) && auth == null) {
//            //如果账号不为空 并且 认证信息是空的,获取用户信息
//            UserDetails userDetails = userDetailsServiceHandler.loadUserByUsername(accountName);
//            if (accountName.equals(userDetails.getUsername())) {
//                //如果用户信息不为空
//                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//                //创建用户 认证token 对象
//                UsernamePasswordAuthenticationToken upt =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                //把web的请求信息 放到Details
//                upt.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                //把用户信息放到 安全上下文中
//                securityContext.setAuthentication(upt);
//                SecurityContextHolder.setContext(securityContext);
//            }
//        }

        //放行
        filterChain.doFilter(request, response);
    }


}
