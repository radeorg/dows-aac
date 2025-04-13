package org.dows.aac.api;


import org.dows.aac.request.BindingUserRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public interface AacApi {
    @GetMapping("/v1/aac/user/info")
    AacUser getCurrentAacUser();

    @PostMapping("/v1/aac/user/binding")
    AacUser bindingCurrentAacUser(BindingUserRequest bindingUserRequest);

    //当rbac域的角色发送变化时，同步账号权限
    //void syncPermission(SyncAccountPermissionRequest syncAccountPermissionRequest);



}
