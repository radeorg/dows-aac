package org.dows.aac.security.config;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.AacUser;
import org.dows.aac.api.AacVerifyConfig;
import org.dows.aac.security.AacAccessDeniedHandler;
import org.dows.aac.security.AacLogoutHandler;
import org.dows.aac.security.endpoint.AacAuthenticationEntryPoint;
import org.dows.aac.security.filter.HandlerExceptionResolverFilter;
import org.dows.aac.security.filter.JwtAuthenticationFilter;
import org.dows.rade.status.AuthStatusCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 认证配置
 */
@RequiredArgsConstructor
@EnableWebSecurity
@Slf4j
@Configuration
public class AuthenticationConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AacAuthenticationEntryPoint aacAuthenticationEntryPoint;
    // 拒绝
    private final AacAccessDeniedHandler aacAccessDeniedHandler;
    // 登出
    private final AacLogoutHandler aacLogoutHandler;
    // filter 异常处理
    private final HandlerExceptionResolverFilter handlerExceptionResolverFilter;

//    private final AacUsernamePasswordAuthenticationProvider aacUsernamePasswordAuthenticationProvider;

    private final UserDetailsService userDetailsServiceHandler;

    private final AacVerifyConfig aacVerifyConfig;

//    private final RbacApi rbacApi;

    //密码加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //安全过滤器链
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /**
         * 每次都需要认证 不需要session 会话管理策略设置为无状态，这样就可以防止应用程序的会话被劫持攻击
         * 禁用session,前后端分离不需要, cookie中就不会显示JSESSIONID
         */
        return http
                // 无论是否登录都可以访问
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //先进行jwt 校验 在进行账号密码登录
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                //动态拦截所有请求 如果没有匹配上的 那么就拦截(白名单放行)
                .authorizeHttpRequests(x -> x.requestMatchers(aacVerifyConfig.getWhitelist())
                                .permitAll()
                                .requestMatchers(HttpMethod.OPTIONS)
                                .permitAll()
                                .anyRequest()
//                        .authenticated()
                                .access((authentication, object) -> {
                                    if (!aacVerifyConfig.isEnableLogin()) {
                                        return new AuthorizationDecision(true);
                                    } else {
                                        Authentication ac = authentication.get();
                                        try {
                                            AacUser aacUser = (AacUser) ac.getPrincipal();
                                            if (aacUser.isSuperAccount()) {
                                                // 超级管理员放行
                                                return new AuthorizationDecision(true);
                                            }
                                        } catch (Exception e) {
                                            //InvalidBearerTokenException
                                            //CredentialsExpiredException
                                            //AuthenticationServiceException
                                            throw new CredentialsExpiredException(AuthStatusCode.UNAUTHORIZED.getDescribe());
                                        }
                                        Collection<? extends GrantedAuthority> authorities = ac.getAuthorities();
                                        //表示请求的 URL 地址和数据库的地址是否匹配上了
                                        boolean isMatch = false;
                                        //获取当前请求的 URL 地址
                                        String requestURI = object.getRequest().getRequestURI();
                                        Map<String, List<RbacUriRoleResponse>> result = rbacApi.getRoleUri();
                                        if (CollectionUtil.isNotEmpty(result)) {
                                            Set<String> uris = result.keySet();
                                            for (String uri : uris) {
                                                if (uri.equals(requestURI)) {
                                                    isMatch = true;
                                                    List<RbacUriRoleResponse> rbacUriRoleResponses = result.get(uri);
//                                            Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
                                                    for (GrantedAuthority authority : authorities) {
                                                        for (RbacUriRoleResponse rbacUriRoleRespons : rbacUriRoleResponses) {
                                                            if (authority.getAuthority().equals(String.valueOf(rbacUriRoleRespons.getRoleId()))) {
                                                                //说明当前登录用户具备当前请求所需要的角色
                                                                return new AuthorizationDecision(true);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (!isMatch) {
                                            //说明请求的 URL 地址和数据库的地址没有匹配上，对于这种请求，统一只要登录就能访问
                                            if (authentication.get() instanceof AnonymousAuthenticationToken) {
                                                return new AuthorizationDecision(false);
                                            } /*else {
                                        //说明用户已经认证了
                                        return new AuthorizationDecision(true);
                                    }*/
                                        }
                                        return new AuthorizationDecision(false);
                                    }
                                })
                )
                //禁用表单登录 前后分离用不上
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(handlerExceptionResolverFilter, CorsFilter.class)
                //没有登录 直接返回异常信息
                .exceptionHandling(x -> x.authenticationEntryPoint(aacAuthenticationEntryPoint)
                        .accessDeniedHandler(aacAccessDeniedHandler))
//                .authenticationProvider(aacUsernamePasswordAuthenticationProvider)
                //关闭csrf
                .csrf(AbstractHttpConfigurer::disable)
                //登录配置[登出页|登出处理器|...]
                .logout(x -> x.addLogoutHandler(aacLogoutHandler))
                .build();

    }

    /**
     * 配置springsecurity的放行路径等信息
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        String[] whitelist = aacVerifyConfig.getWhitelist();
        //放行登录接口 这样才能登录成功
        return x -> x.ignoring().requestMatchers(whitelist);
    }


    /**
     * 配置认证管理器，security框架默认不提供
     * 把认证管理器注入到容器 LoginHandler类中才能使用这个认证接口
     * public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
     * return config.getAuthenticationManager();
     * }
     *
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        //return config.getAuthenticationManager();
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //设置securityUserDetailService，告知security框架，按照指定的类进行身份校验
        daoAuthenticationProvider.setUserDetailsService(userDetailsServiceHandler);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        //AacCellphoneCodeAuthenticationProvider mobilePhoneVerificationCodeProvider = new AacCellphoneCodeAuthenticationProvider();
        //mobilePhoneVerificationCodeProvider.setUserDetailsService(userDetailsServiceHandler);
        ProviderManager pm = new ProviderManager(daoAuthenticationProvider/*, mobilePhoneVerificationCodeProvider*/);
        return pm;
    }


}
