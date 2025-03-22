package org.dows.aac.api;

import org.dows.aac.api.response.RbacUriRoleResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface RbacApi {
    List<String> getAllUri(String appId);

    List<String> getUriCode(List<Long> longs);

    Map<String, List<RbacUriRoleResponse>> getRoleUri();
}
