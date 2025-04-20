package org.dows.aac.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dows.rade.constant.IdentifierType;

@Data
public class GetAccessTokenRequest {
    @Schema(description = "标识类型[账号，手机，邮箱,微信，QQ，微博......]")
    private IdentifierType identifierType;

    @Schema(description = "应用ID")
    private String appId;
    // 其他参数
}
