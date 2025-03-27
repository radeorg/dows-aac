package org.dows.aac.security.provider;

import lombok.RequiredArgsConstructor;
import org.dows.aac.security.UserDetailsServiceHandler;
import org.dows.uim.api.AccountApi;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 实现身份验证提供程序
 *
 * @param
 * @return
 * @throws Exception
 */
/*@Slf4j
@Component
@RequiredArgsConstructor*/
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final AccountApi accountApi;
    private final UserDetailsServiceHandler userDetailsServiceHandler;
    private final PasswordEncoder passwordEncoder;


    /**
     * LoginServiceImpl的登录方法点击认证的时候 直接跳转到这里
     * 登陆认证
     *
     * @param
     * @return
     * @throws Exception
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //从authentication获取用户名和凭证(密码)信息
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = userDetailsServiceHandler.loadUserByUsername(username);
        // 如果为空则注册
        if (userDetails == null) {
            //userDetailsServiceHandler.newRegister();
        }
        //Long accountInstanceId = accountApi.getAccountIdWithRegister(appId, s);
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            //因为UsernamePasswordAuthenticationToken的上级父类的父类是Authentication 所以可以直接返回
            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("用户名或者密码错误");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //保证认证和返回的对象都是UsernamePasswordAuthenticationToken
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}