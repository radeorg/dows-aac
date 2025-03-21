package org.dows.aac.api;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RbacApi {
    List<String> getAllUri(String appId);

    List<String> getUriCode(List<Long> longs);
}
