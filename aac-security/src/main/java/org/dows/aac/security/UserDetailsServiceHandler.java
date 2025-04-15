package org.dows.aac.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.handler.rbac.RbacApiHandler;
import org.dows.aac.handler.uim.UimApiHandler;
import org.dows.aac.request.LoginRequest;
import org.dows.rade.context.AppContext;
import org.dows.rbac.model.RoleResourceResponse;
import org.dows.rbac.response.RbacUriResponse;
import org.dows.uim.response.AccountInstanceResponse;
import org.dows.uim.response.RootOrgResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private final UimApiHandler uimApiHandler;
    private final RbacApiHandler rbacApiHandler;
//    private final AccountApi accountApi;
//    private final RbacApi rbacApi;
//    private final AacContext aacContext;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (StrUtil.isBlank(s)) {
            throw new UsernameNotFoundException("不存在账号标识为空的账号");
        }
        log.info("根据账号标识:{}查询账号信息", s);
        String appId = AppContext.getAppId();
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
        AccountInstanceResponse accountInstanceResponse = uimApiHandler.getAccountInstanceByIdentifier(appId, s);
        if (null == accountInstanceResponse) {
            log.info("账号不存在");
            return null;
        }
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        List<Long> roleIds = null;
        // 超管
        if (accountInstanceResponse.isSuperAccount()) {
            // 超级管理员角色Id默认1L
            Long roleId = 1L;
            roleIds = Collections.singletonList(roleId);
            // 获取所有资源
            List<RbacUriResponse> authority = rbacApiHandler.getAllUrisByAppId(appId);
            Map<String,Object> roleInfo = new HashMap<>();
            roleInfo.put(String.valueOf(roleId),authority);
            grantedAuthorityList.add(new OAuth2UserAuthority(String.valueOf(roleId),roleInfo));
        }else{
            // 获取账号及在所在组织的所有角色ID->根据角色ID获取对应的菜单&资源信息 组装权限信息 放入 GrantedAuthority,
            roleIds = uimApiHandler.getAllRoleIds(appId, accountInstanceResponse.getAccountInstanceId());

            if(CollectionUtil.isNotEmpty(roleIds)){
                //for (Long roleId : roleIds) {
                List<RoleResourceResponse> roleResourceResponses= rbacApiHandler.getUrisByRoleIds(appId, roleIds);
                    //List<String> authority = rbacApi.getUriCode(Collections.singletonList(roleId));
                    //Map<String,Object> roleInfo = new HashMap<>();
                    //roleInfo.put(String.valueOf(roleId),authority);
                for (RoleResourceResponse rr : roleResourceResponses) {
                    Map<String, Object> roleInfo = new HashMap<>();
                    roleInfo.put(String.valueOf(rr.getRoleId()), rr.getAuthority());
                    grantedAuthorityList.add(new OAuth2UserAuthority(String.valueOf(rr.getRoleId()), roleInfo));
                }
                //}
            }
        }
        //把权限放入用户对象中
        DefaultAacUser defaultAacUser = new DefaultAacUser(accountInstanceResponse.getAccountInstanceId(),
                accountInstanceResponse.getIdentifier(),
                accountInstanceResponse.getPassword(),
                grantedAuthorityList, roleIds, accountInstanceResponse.isSuperAccount());

        try {
            List<RootOrgResponse> orgRootIdResponse = uimApiHandler.getOrgRootId(accountInstanceResponse.getAccountInstanceId());
            List<Integer> accountTypes = uimApiHandler.getAccountTypes(accountInstanceResponse.getAccountInstanceId());
            // 设置账号所在组织根节点ID
            List<Long> orgIds = orgRootIdResponse.stream().map(RootOrgResponse::getRootOrgId).toList();
            defaultAacUser.setOrgRootIds(orgIds);
            RootOrgResponse rootOrgResponse = orgRootIdResponse.stream().filter(RootOrgResponse::isDefaultOrg).findFirst()
                    .orElse(null);
            if (null != rootOrgResponse) {
                defaultAacUser.setOrgRootId(orgRootIdResponse.get(0).getRootOrgId());
            }
            // 设置账号类型 @org.dows.uim.constant.AccountType
            defaultAacUser.setAccountTypes(accountTypes);
        } catch (Exception e) {
            log.error("获取账号所在组织根节点ID及账号类型失败", e);
        }
        log.debug("{}", defaultAacUser);
        return defaultAacUser;
    }

    @Transactional
    public void newRegister(String name, String encode, LoginRequest loginRequest) {
        uimApiHandler.newRegister(name, encode, loginRequest);
    }
}

