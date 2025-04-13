package org.dows.aac.weixin;

import lombok.Data;
import org.dows.aac.api.OpenResponse;

@Data
public class OpenidResponse implements OpenResponse {
    private String openid;
    private String session_key;
    private String unionid;
    private int errcode;
    private String errmsg;

}
