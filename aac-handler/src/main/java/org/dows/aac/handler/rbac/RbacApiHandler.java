package org.dows.aac.handler.rbac;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.rbac.api.RbacApi;
import org.dows.rbac.model.RoleResourceResponse;
import org.dows.rbac.response.RbacUriResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RbacApiHandler {

    private final RbacApi rbacApi;

    public List<RbacUriResponse> getAllUrisByAppId(String appId) {
        return rbacApi.getAllUrisByAppId(appId);
    }

    public List<RoleResourceResponse> getUrisByRoleIds(String appId, List<Long> roleIds) {
        return rbacApi.getUrisByRoleIds(appId, roleIds);
    }
}
