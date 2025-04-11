package org.dows.aac.handler.alipay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.aac.api.ApiMapping;
import org.dows.aac.constant.OpenApiEnum;
import org.dows.rade.constant.OpenChannel;

@RequiredArgsConstructor
@Slf4j
@ApiMapping(channel = OpenChannel.ACCOUNT, func = OpenApiEnum.GET_OPENID)
public class AccountLoginHandler {
}
