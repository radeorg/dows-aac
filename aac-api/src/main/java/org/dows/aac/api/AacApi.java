package org.dows.aac.api;


public interface AacApi {


//
//    /**
//     * 当rbac域的角色发送变化时，同步账号权限
//     */
//    void syncAccountPermission(SyncAccountPermissionRequest syncAccountPermissionRequest);

    AacUser getCurrentAccUser();

//    AacUser getAccUser();
}
