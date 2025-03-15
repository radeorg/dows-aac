package org.dows.aac.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 3/18/2024 1:49 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Component
public class AacLogoutHandler implements LogoutHandler {


    /**
     * 登出实现
     *
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //先获取token
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        //从db或者缓存中获取token并且对比
        /*Token token = null;
        if (token != null) {
            token.setExpired(true);
            token.setRevoked(true);
            token.insertOrUpdate();
            SecurityContextHolder.clearContext();
        }*/

    }
}