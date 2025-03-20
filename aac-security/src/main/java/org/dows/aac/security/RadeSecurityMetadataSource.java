package org.dows.aac.security;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.rade.enums.UserTypeEnum;
import org.dows.rade.rbac.RbacProvider;
import org.dows.rade.security.SecurityProvider;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限资源管理器 为权限决断器提供支持
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RadeSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    final private SecurityProvider securityProvider;

    final private RbacProvider rbacProvider;

    private Map<String, Collection<ConfigAttribute>> map = null;

    /**
     * 加载权限表中所有操作请求权限
     */
    public void loadResourceDefine() {
        map = new HashMap<>();
        Collection<ConfigAttribute> configAttributes;
        ConfigAttribute cfg;
        String[] perms = rbacProvider.getAllPerms();
        // 获取启用的权限操作请求
        for (String perm : perms) {
            configAttributes = new ArrayList<>();
            cfg = new SecurityConfig(perm);
            // 作为MyAccessDecisionManager类的decide的第三个参数
            configAttributes.add(cfg);
            // 用权限的path作为map的key，用ConfigAttribute的集合作为value
            map.put(perm.replaceAll(":", "/"), configAttributes);
        }
    }

    /**
     * 判定用户请求的url是否在权限表中 如果在权限表中，则返回给decide方法，用来判定用户是否有此权限 如果不在权限表中则放行
     *
     * @param o
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        UserTypeEnum userTypeEnum = securityProvider.getCurrentUserType();
        if (ObjectUtil.equal(userTypeEnum, UserTypeEnum.APP)) {
            // app用户不需要权限拦截
            return null;
        }
        if (map == null) {
            loadResourceDefine();
        }
        // Object中包含用户请求request
        String url = ((FilterInvocation) o).getRequestUrl();
        return map.get(url.replace("/admin/", "").split("[?]")[0]);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<>();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
