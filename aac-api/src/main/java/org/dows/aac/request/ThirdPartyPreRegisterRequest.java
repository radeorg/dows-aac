package org.dows.aac.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dows.rade.constant.IdentifierType;

@Schema(description = "三方预注册请求")
@Data
public class ThirdPartyPreRegisterRequest {

    @Schema(description = "标识类型[账号，手机，邮箱,微信，QQ，微博......]")
    private IdentifierType identifierType;
    @Schema(description = "第三方加密CODE用于获取用户数据[手机号,性别,年龄,省份,城市,头像地址,......]")
    private String codeForData;
    @Schema(description = "用户获取第三方openid的CODE[微信，QQ，微博......]")
    private String codeForOpenid;

    @Schema(description = "mock telephone,用于测试")
    private String mockTelephone;
    @Schema(description = "mock openid,用于测试")
    private String mockOpenid;

}
