package org.dows.aac.request;

import lombok.Data;

@Data
public class GetTokenRequest {
    private String redirectUri;
    private String code;
}
