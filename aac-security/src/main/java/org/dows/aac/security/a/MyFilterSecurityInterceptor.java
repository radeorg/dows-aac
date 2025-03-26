//package org.dows.aac.security;
//
//import jakarta.annotation.Resource;
//import jakarta.servlet.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.access.SecurityMetadataSource;
//import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
//import org.springframework.security.access.intercept.InterceptorStatusToken;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
//
//import java.io.IOException;
//
///**
// * 权限管理拦截器 监控用户行为
// */
//@Slf4j
////@Component
//@RequiredArgsConstructor
//public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
//
//    final private FilterInvocationSecurityMetadataSource securityMetadataSource;
//
//    @Resource
//    public void setMyAccessDecisionManager(MyAccessDecisionManager myAccessDecisionManager) {
//        super.setAccessDecisionManager(myAccessDecisionManager);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        FilterInvocation fi = new FilterInvocation(request, response, chain);
//        invoke(fi);
//    }
//
//    public void invoke(FilterInvocation fi) throws IOException, ServletException {
//        InterceptorStatusToken token = super.beforeInvocation(fi);
//        try {
//            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
//        } finally {
//            super.afterInvocation(token, null);
//        }
//    }
//
//    @Override
//    public void destroy() {
//    }
//
//    @Override
//    public Class<?> getSecureObjectClass() {
//        return FilterInvocation.class;
//    }
//
//    @Override
//    public SecurityMetadataSource obtainSecurityMetadataSource() {
//        return this.securityMetadataSource;
//    }
//}