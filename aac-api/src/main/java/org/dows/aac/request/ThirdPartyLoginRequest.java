package org.dows.aac.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dows.rade.constant.IdentifierType;

@Schema(description = "Third party login request")
@Data
public class ThirdPartyLoginRequest {

    @Schema(description = "标识类型[账号，手机，邮箱,微信，QQ，微博......]")
    private IdentifierType identifierType;
    // 加密后的用户标识
    @Schema(description = "验证CODE")
    // 验证码[微信code,短信，邮箱]
    private String verifyCode;

}
