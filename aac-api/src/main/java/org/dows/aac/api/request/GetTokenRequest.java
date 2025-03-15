package org.dows.aac.api.request;

import lombok.Data;

@Data
public class GetTokenRequest {
    private String redirectUri;
    private String code;
}
