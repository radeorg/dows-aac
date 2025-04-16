package org.dows.aac.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response for third party accreditation")
public class ThirdPartyAccreditResponse {

    @Schema(description = "openid")
    private String openid;
    @Schema(description = "telephone")
    private String telephone;
    @Schema(description = "nickname")
    private String nickname;
    @Schema(description = "avatar")
    private String avatar;
    @Schema(description = "gender")
    private String gender;
    @Schema(description = "country")
    private String country;
    @Schema(description = "province")
    private String province;
    @Schema(description = "city")
    private String city;
    @Schema(description = "zoneNo")
    private String zoneNo;
    @Schema(description = "状态State")
    private Integer state;


}
