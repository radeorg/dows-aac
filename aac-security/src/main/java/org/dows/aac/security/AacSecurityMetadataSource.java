package org.dows.aac.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 3/18/2024 6:33 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AacSecurityMetadataSource implements SecurityMetadataSource {
    /**
     * 当前系统所有url资源
     */
    private final List<String> resources;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        log.info("---MySecurityMetadataSource---");
        // 该对象是Spring Security帮我们封装好的，可以通过该对象获取request等信息
        FilterInvocation filterInvocation = (FilterInvocation) object;
        HttpServletRequest request = filterInvocation.getRequest();
        // 遍历所有权限资源，以和当前请求所需的权限进行匹配
        for (String resource : resources) {
            // 因为我们url资源是这种格式：api/user/test/{id}，冒号前面是请求方法，冒号后面是请求路径，所以要字符串拆分
            // 因为/API/user/test/{id}这种路径参数不能直接equals来判断请求路径是否匹配，所以需要用Ant类来匹配
//            AntPathRequestMatcher ant = new AntPathRequestMatcher(split[1]);
//            // 如果请求方法和请求路径都匹配上了，则代表找到了这个请求所需的权限资源
//            if (request.getMethod().equals(split[0]) && ant.matches(request)) {
//                // 将我们权限资源id返回
//                return Collections.singletonList(new SecurityConfig(resource.getId().toString()));
//            }
        }
        // 走到这里就代表该请求无需授权即可访问，返回空
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}

