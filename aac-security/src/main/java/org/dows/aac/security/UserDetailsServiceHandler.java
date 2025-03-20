package org.dows.aac.security;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.AacUser;
import org.dows.app.api.AppApi;
import org.dows.app.api.AppContext;
import org.dows.app.api.StateEnum;
import org.dows.rbac.api.RbacApi;
import org.dows.rbac.api.admin.response.RbacPermissionResponse;
import org.dows.uat.api.AccountApi;
import org.dows.uat.api.admin.request.FindAccountIdentifierRequest;
import org.dows.uat.api.admin.response.AccountIdentifierResponse;
import org.dows.uat.api.admin.response.AccountInstanceResponse;
import org.dows.uat.api.admin.response.AccountOrgIdsResponse;
import org.dows.uat.api.admin.response.AccountRoleRelationResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
    private final AppApi appApi;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("根据账号标识查询账号信息");
        String appId = AppContext.getAppId();
        AccountInstanceResponse accountInstanceResponse = accountApi.getAccountInstanceByAccountName(s,appId );
        Long accountInstanceId = null;
        if(null == accountInstanceResponse){
            // 账号未查到信息可以通过其他账号标识[邮箱，电话]
            FindAccountIdentifierRequest findAccountIdentifierRequest = new FindAccountIdentifierRequest();
            findAccountIdentifierRequest.setIdentifier(s);
            // 前端需要传账号标识
            findAccountIdentifierRequest.setIdentifierType(null);
            findAccountIdentifierRequest.setState(StateEnum.AVAILABLE.getCode());
            findAccountIdentifierRequest.setAppId(appId);
            AccountIdentifierResponse accountIdentifier = accountApi.getAccountIdentifier(findAccountIdentifierRequest);
            if (accountIdentifier == null) {
                log.info("账号标识不存在");
                throw new UsernameNotFoundException("账号标识不存在");
            }
            accountInstanceResponse = accountApi.getAccountInstanceById(accountIdentifier.getAccountInstanceId());
            if(null == accountInstanceResponse){
                log.info("账号不存在");
                throw new UsernameNotFoundException("账号不存在");
            }
        }
        accountInstanceId = accountInstanceResponse.getAccountInstanceId();
        List<GrantedAuthority> list = new ArrayList<>();
        List<Long> roleIds = null;
        if(accountInstanceResponse.isSuperAccount()){
            // 超级管理员角色Id默认1L
            Long roleId = 1L;
            roleIds = Collections.singletonList(roleId);
            List<String> authority = appApi.getAllUri(appId);
            Map<String,Object> roleInfo = new HashMap<>();
            roleInfo.put(String.valueOf(roleId),authority);
            list.add(new OAuth2UserAuthority(String.valueOf(roleId),roleInfo));
        }else{
            //根据账号id获取菜单&权限信息 组装权限信息 放入 GrantedAuthority
            roleIds = getAuthList(accountInstanceId, s);
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
        //accountInstanceResponse.setMenu(auths);
        return new AacUser(accountInstanceId,accountInstanceResponse.getAccountName(),
                accountInstanceResponse.getPassword(),
                list,roleIds,accountInstanceResponse.isSuperAccount());
    }


    /**
     * 组装权限信息 放入 SimpleGrantedAuthority
     *
     * @return
     */
    private List<GrantedAuthority> getGrantedAuthority(List<RbacPermissionResponse> auths) {
        List<GrantedAuthority> list = new ArrayList<>();
        for (RbacPermissionResponse x : auths) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(x.getAuthority());
            list.add(grantedAuthority);
        }
        return list;
    }


    /**
     * 根据账号id获取所有权限
     * 在分配权限的时候 如果只选择了按,那么也会把他的父级菜单给查询出,只有按钮都不选择的时候 菜单才不展示
     *
     * @param accountInstanceId
     * @return
     */
    private List<Long> getAuthList(Long accountInstanceId, String accountName) {
        String appId = AppContext.getAppId();
        // 获取组织
        AccountOrgIdsResponse orgIdsByAccountId = accountApi.getOrgIdsByAccountId(accountInstanceId, false,appId);
        List<Long> orgIds = orgIdsByAccountId.getAccountOrgId();
        List<Long> principals = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orgIds)) {
            principals.addAll(orgIds);
        }
        principals.add(accountInstanceId);
        // 查询角色
        List<AccountRoleRelationResponse> accountRoleRelationResponses = accountApi
                .getRoleByAccountInstanceId(principals,appId);
        if (CollectionUtil.isNotEmpty(accountRoleRelationResponses)) {
            return accountRoleRelationResponses.stream().map(AccountRoleRelationResponse::getRbacRoleId).toList();
        }
        return new ArrayList<>();
        // 个人账号角色加组织账号角色

//        Map<String,Object> userInfo = new HashMap<>();
//        userInfo.put("roleIds",roleIds);
//        userInfo.put("accountInstanceId",accountInstanceId);
//        aacCache.putCache(UserInfoEnum.USER_INFO.getKey(),accountName,userInfo);
//        List<GrantedAuthority> list = new ArrayList<>();
//        if(CollectionUtil.isNotEmpty(roleIds)){
//            for (Long roleId : roleIds) {
//                List<String> authority = rbacApi.getUriCode(Collections.singletonList(roleId));
//                Map<String,Object> roleInfo = new HashMap();
//                roleInfo.put(String.valueOf(roleId),authority);
//                list.add(new OAuth2UserAuthority(String.valueOf(roleId),roleInfo));
//            }
//        }
//        return list;
//        return CollectionUtil.newArrayList(new SimpleGrantedAuthority(String.valueOf(accountInstanceId)));
    }
}

