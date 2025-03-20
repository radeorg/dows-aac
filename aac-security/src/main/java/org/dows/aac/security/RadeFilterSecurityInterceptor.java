package org.dows.aac.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

/**
 * 权限管理拦截器 监控用户行为
 */
@Slf4j
//@Component
//@RequiredArgsConstructor
public class RadeFilterSecurityInterceptor extends AuthorizationFilter  {

    //final private FilterInvocationSecurityMetadataSource securityMetadataSource;

    /**
     * Creates an instance.
     *
     * @param authorizationManager the {@link AuthorizationManager} to use
     */
    public RadeFilterSecurityInterceptor(AuthorizationManager<HttpServletRequest> authorizationManager) {
        super(authorizationManager);
    }


}