package org.dows.aac.api;


import org.dows.aac.request.BindingUserRequest;
import org.dows.aac.request.SyncAccountPermissionRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public interface AacApi {

    /**
     * 获取当前登录人信息
     *
     * @return
     */
    @GetMapping("/v1/open/aac/account/get")
    AacUser getCurrentAacUser();


    /**
     * 绑定当前账号相关信息
     *
     * @param bindingUserRequest
     * @return
     */
    @PostMapping("/v1/open/aac/account/binding")
    AacUser bindingCurrentAacUser(BindingUserRequest bindingUserRequest);

    /**
     * 认领确认当前登录人信息
     *
     * @param bindingUserRequest
     * @return
     */
    @PostMapping("/v1/open/aac/account/claim")
    default AacUser claimCurrentAacUser(BindingUserRequest bindingUserRequest) {
        throw new UnsupportedOperationException();
    }

    /**
     * 当rbac域的角色发送变化时，同步账号权限
     *
     * @param syncAccountPermissionRequest
     */
    @PostMapping("/v1/open/aac/permission/sync")
    default void syncPermission(SyncAccountPermissionRequest syncAccountPermissionRequest) {
        throw new UnsupportedOperationException();
    }


//    /**
//     * 第三方openid登录时的预注册，此时只创建accountIdentifier表，并不创建accountInstance表
//     * @param bindingUserRequest
//     * @return
//     */
//    @PostMapping("/v1/aac/thirdparty/phone/get")
//    default AacUser preRegister(@RequestBody BindingUserRequest bindingUserRequest) {
//        throw new UnsupportedOperationException();
//    }

//    /**
//     * 第三方授权手机号匹配系统账号
//     * @param bindingUserRequest
//     * @return
//     */
//    @PostMapping("/v1/aac/account/phone/matching")
//    default AacUser matchingAccount(@RequestBody BindingUserRequest bindingUserRequest) {
//        throw new UnsupportedOperationException();
//    }
}
