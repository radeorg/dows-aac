package org.dows.aac.api;


import org.springframework.web.bind.annotation.GetMapping;

public interface AacApi {
    @GetMapping("/v1/aac/user/info")
    AacUser getCurrentAccUser();

    //当rbac域的角色发送变化时，同步账号权限
    //void syncPermission(SyncAccountPermissionRequest syncAccountPermissionRequest);



}
