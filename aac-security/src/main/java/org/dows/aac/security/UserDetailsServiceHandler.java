package org.dows.aac.security;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.AacContext;
import org.dows.aac.api.request.LoginRequest;
import org.dows.rbac.api.RbacApi;
import org.dows.rbac.api.admin.response.RbacUriResponse;
import org.dows.uim.api.AccountApi;
import org.dows.uim.api.request.AccountInstanceRequest;
import org.dows.uim.api.response.AccountInstanceResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 2/28/2024 9:55 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class UserDetailsServiceHandler implements UserDetailsService {

    private final AccountApi accountApi;
    private final RbacApi rbacApi;
    private final AacContext aacContext;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("根据账号标识查询账号信息");
        String appId = aacContext.getAppId();
        /**
         * 根据账号标识查询账号信息,此时登录即注册，注册即登录,账号未查到信息可以通过其他账号标识[邮箱，电话]
         *             FindAccountIdentifierRequest findAccountIdentifierRequest = new FindAccountIdentifierRequest();
         *             findAccountIdentifierRequest.setIdentifier(s);
         *             // 前端需要传账号标识
         *             findAccountIdentifierRequest.setIdentifierType(null);
         *             findAccountIdentifierRequest.setState(StateEnum.AVAILABLE.getCode());
         *             findAccountIdentifierRequest.setAppId(appId);
         *             AccountIdentifierResponse accountIdentifier = accountApi.getAccountIdentifier(findAccountIdentifierRequest);
         *             if (accountIdentifier == null) {
         *                 log.info("账号标识不存在");
         *                 throw new UsernameNotFoundException("账号标识不存在");
         *             }
         *             accountInstanceResponse = accountApi.getAccountInstanceById(accountIdentifier.getAccountInstanceId());
         *             if(null == accountInstanceResponse){
         *                 log.info("账号不存在");
         *                 throw new UsernameNotFoundException("账号不存在");
         *             }
         */
        AccountInstanceResponse accountInstanceResponse = accountApi.getAccountInstanceId(appId, s);
        if (null == accountInstanceResponse) {
            log.info("账号不存在");
            return null;
        }
        List<GrantedAuthority> list = new ArrayList<>();
        List<Long> roleIds = null;
        // 超管
        if(accountInstanceResponse.isSuperAccount()){
            // 超级管理员角色Id默认1L
            Long roleId = 1L;
            roleIds = Collections.singletonList(roleId);
            // 获取所有资源
            List<RbacUriResponse> authority = rbacApi.getAllUri(appId);
            Map<String,Object> roleInfo = new HashMap<>();
            roleInfo.put(String.valueOf(roleId),authority);
            list.add(new OAuth2UserAuthority(String.valueOf(roleId),roleInfo));
        }else{
            // 获取账号及在所在组织的所有角色ID->根据角色ID获取对应的菜单&资源信息 组装权限信息 放入 GrantedAuthority,
            roleIds = accountApi.getAllRoleIds(appId, accountInstanceResponse.getAccountInstanceId());
            if(CollectionUtil.isNotEmpty(roleIds)){
                for (Long roleId : roleIds) {
                    List<String> authority = rbacApi.getUriCode(Collections.singletonList(roleId));
                    Map<String,Object> roleInfo = new HashMap<>();
                    roleInfo.put(String.valueOf(roleId),authority);
                    list.add(new OAuth2UserAuthority(String.valueOf(roleId),roleInfo));
                }
            }
        }
        //把权限放入用户对象中
        DefaultAacUser defaultAacUser = new DefaultAacUser(accountInstanceResponse.getAccountInstanceId(),
                accountInstanceResponse.getAccountName(),
                accountInstanceResponse.getPassword(),
                list, roleIds, accountInstanceResponse.isSuperAccount());
        log.debug("{}", defaultAacUser);
        return defaultAacUser;
    }

    public void newRegister(String name, String encode, LoginRequest loginRequest) {
        AccountInstanceRequest accountInstanceRequest = new AccountInstanceRequest();
        accountInstanceRequest.setPassword(encode);
        accountInstanceRequest.setIdentifier(name);
        accountInstanceRequest.setIdentifierType(loginRequest.getIdentifierType());
        accountInstanceRequest.setAppId(loginRequest.getAppId());
        accountInstanceRequest.setZoneNo(loginRequest.getZoneNo());
        accountInstanceRequest.setCellphone(name);
        accountInstanceRequest.setAvator(loginRequest.getAvator());
        accountInstanceRequest.setSource(loginRequest.getSource());
        accountInstanceRequest.setReferralsNo(loginRequest.getReferralsNo());

        accountApi.setAccountInstance(accountInstanceRequest);
    }
}

