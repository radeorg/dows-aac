package org.dows.aac.api;

import java.util.List;


public interface AacUser {

    // 账号ID
    Long getAccountId();

    // 用户ID
    Long getUserId();

    // 角色集ID
    List<Long> getRoleIds();
    //void setRoleIds(List<Long> roleIds);

    // 账号类型
    List<Integer> getAccountTypes();
    //void setAccountTypes(List<Integer> accountTypes);

    // 超级账号
    boolean isSuperAccount();

    //账号名
    String getNickname();

    //用户名
    String getUsername();

    // 头像
    String getAvatar();

    //手机
    String getPhone();
    //void setPhone(String phone);

    // 邮箱
    String getEmail();
    //void setEmail(String email);
}
