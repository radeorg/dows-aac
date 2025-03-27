package org.dows.aac.security.filter;

import cn.hutool.jwt.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.AacSettings;
import org.dows.aac.api.AacContext;
import org.dows.aac.api.AacException;
import org.dows.aac.api.Cacheable;
import org.dows.aac.api.constant.UserInfoEnum;
import org.dows.aac.security.UserDetailsServiceHandler;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 *  Token过滤器,只登陆1次
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceHandler userDetailsServiceHandler;

    private final AacSettings aacSettings;

    private final Cacheable cacheable;

    private final AacContext aacContext;

    /*private List<String> whiteList;AacContext
    @PostConstruct
    public void init() {
        whiteList = Arrays.stream(aacSettings.getWhitelist()).toList();
    }*/

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
        long count = Arrays.stream(aacSettings.getWhitelist())
                .filter(w -> w.equalsIgnoreCase(request.getRequestURI()))
                .count();
        if (count > 0) {
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
        String token = request.getHeader(aacSettings.getJwtSetting().getHeader());
        String appId = request.getHeader("AppId");
        if (appId != null && appId.isBlank()) {
            throw new AacException("appId不能为空");
        }
        // 设置appId
        aacContext.setAppId(appId);
        log.debug("Bearer token :{}", token);
        //注意Bearer后面还有一个空格
        if (!StringUtils.hasLength(token) || !StringUtils.startsWithIgnoreCase(token, "Bearer ")) {
            // 不需要登录验证
            if (!aacSettings.isEnableLogin()) {
                filterChain.doFilter(request, response);
                return;
            }
            //如果请求头是空的 或者 前置没有以Bearer 开头 那么进入下一个过滤器链
            filterChain.doFilter(request, response);
            return;
        }
        token = token.substring(7);
        //JWTUtil.verify(token, aacProperties.getJwtSetting().getSecretKey());

        Object o = cacheable.getCacheValue(UserInfoEnum.SECURITY_CONTEXT.getKey(), token);

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



    /**
     * 处理app请求
     */
    private void handlerAppRequest(HttpServletRequest request, JWT jwt, String authToken) {
        /*String userId = jwt.getPayload("userId").toString();
        if (ObjectUtil.isNotEmpty(userId)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = radeCache.get("app:userDetails:" + userId,
                    JwtUser.class);
            if (jwtTokenUtil.validateToken(authToken) && userDetails != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute("userId", jwt.getPayload("userId"));
                request.setAttribute("tokenInfo", jwt);
            }
        }*/
    }

    /**
     * 处理后台请求
     */
    private void handlerAdminRequest(HttpServletRequest request, JWT jwt, String authToken) {
        /*String username = jwt.getPayload("username").toString();
        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = radeCache.get("admin:userDetails:" + username,
                    JwtUser.class);
            Integer passwordV = Convert.toInt(jwt.getPayload("passwordVersion"));
            Integer rv = radeCache.get("admin:passwordVersion:" + jwt.getPayload("userId"),
                    Integer.class);
            if (jwtTokenUtil.validateToken(authToken, username) && Objects.equals(passwordV, rv)
                    && userDetails != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute("adminUsername", jwt.getPayload("username"));
                request.setAttribute("adminUserId", jwt.getPayload("userId"));
                request.setAttribute("tokenInfo", jwt);
            }
        }*/
    }

}
