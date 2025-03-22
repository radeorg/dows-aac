package org.dows.aac.security.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.rade.web.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;


/**
 * @description: </br>
 * 我的身份验证入口点 没有登陆认证 异常处理器
 * @author: lait.zhang@gmail.com
 * @date: 2/28/2024 9:55 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class AacAuthenticationEntryPoint implements AuthenticationEntryPoint {


//    private final AacConfig aacConfig;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        //if (aacConfig.isEnableLogin()) {
            //返回json格式
            response.setContentType("application/json;charset=utf-8");
            //没有登陆 直接访问其他接口 就报401
            response.setStatus(401);
            Response<String> result = Response.failed("401", "请先登录");
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(result);
            //把json数据 写入 返回给前端
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        //}
    }
}