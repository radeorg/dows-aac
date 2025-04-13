package org.dows.aac.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dows.rade.constant.IdentifierType;

@Schema(description = "匹配用户请求")
@Data
public class BindingUserRequest {

    @Schema(description = "标识类型[账号，手机，邮箱,微信，QQ，微博......]")
    private IdentifierType identifierType;
    // 加密后的用户标识
    @Schema(description = "加密的标识用户获取用户信息")
    private String encryptIdentifier;

}
