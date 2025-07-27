package org.dows.aac.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "登录响应")
@Data
public class LoginResponse {
    // token
    @Schema(description = "登录成功后返回的token")
    String token;
    // 账号类型
    @Schema(description = "账号类型[0:普通账号，1:面试官，2:求职者...]")
    List<Integer> accountTypes;
    //状态
    @Schema(description = "状态,0：未邦手机号，1：已邦手机号，2：未邦邮箱，3：已邦邮箱")
    Integer state;
    // 账号标识类型，第三方登录渠道标识，如[weixin, douyin....]
    @Schema(description = "账号标识类型，第三方登录渠道标识，如[weixin, douyin....]")
    Integer identifierType;

    private Long accountIdentifierId;

    @Schema(description = "应用ID")
    private String appId;

    @Schema(description = "组织空间")
    private String namespace;
}
