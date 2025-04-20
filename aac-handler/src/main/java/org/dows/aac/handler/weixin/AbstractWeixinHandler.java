package org.dows.aac.handler.weixin;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.dows.aac.AacSettings;
import org.dows.aac.yml.OpenSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractWeixinHandler {

    protected Map<String, OpenSetting> openSettingMap = new HashMap<>();
    @Resource
    protected AacSettings aacSettings;
/*    @Resource
    protected AacContext aacContext;*/

    @PostConstruct
    public void init() {
        List<OpenSetting> opens = aacSettings.getOpens();
        openSettingMap = opens.stream().collect(Collectors
                .toMap(OpenSetting::getAppId, Function.identity()));
    }


    protected OpenSetting verifyOpenSettingByCurrentAppId(String appId) {
        OpenSetting openSetting = openSettingMap.get(appId);
        if (openSetting == null) {
            throw new RuntimeException("未找到对应的appId");
        }
        return openSetting;
    }
}
