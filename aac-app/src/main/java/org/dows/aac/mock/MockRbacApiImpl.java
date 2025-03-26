package org.dows.aac.mock;

import lombok.extern.slf4j.Slf4j;
import org.dows.rbac.api.InitResources;
import org.dows.rbac.api.InitUriResources;
import org.dows.rbac.api.RbacApi;
import org.dows.rbac.api.admin.request.FindRbacResourcesRequest;
import org.dows.rbac.api.admin.request.SaveRbacRoleRequest;
import org.dows.rbac.api.admin.response.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MockRbacApiImpl implements RbacApi {
    @Override
    public List<RbacRoleResponse> getRole(List<Long> roleIds) {
        return List.of();
    }

    @Override
    public String getMenu() {
        return "";
    }

    @Override
    public List<RbacResourcesQueryResponse> getResource(FindRbacResourcesRequest findRbacResources) {
        return List.of();
    }

    @Override
    public List<RbacPermissionResponse> getPermission(List<Long> roleIds) {
        return List.of();
    }

    @Override
    public List<String> getUriCode(List<Long> roleIds) {
        return List.of();
    }

    @Override
    public void saveResource(List<InitResources> resources) {

    }

    @Override
    public void initRoleUri(List<InitResources> resources, String roleCode, String appId) {

    }

    @Override
    public void initAppRole(List<SaveRbacRoleRequest> roleItems) {

    }

    @Override
    public List<RbacMenusResponse> listRoleMenusTree(List<Long> rbacRoleIds) {
        return List.of();
    }

    @Override
    public void saveUri(List<InitUriResources> initUriResources) {

    }

    @Override
    public Map<String, List<RbacUriRoleResponse>> getRoleUri() {
        return Map.of();
    }
}
