package org.dows.aac.api;

import cn.hutool.core.bean.BeanUtil;

public interface ApiHandler {



    default Object processInputs(Object inputs) {

//        List<String> inputParamNames = WeixinOpenApi.weixinJscode2Sesssion.getInputs();
//        String url = WeixinOpenApi.weixinJscode2Sesssion.getUrl();
//        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(inputs);
//        String uri = "";
//        for (String input : inputParamNames) {
//            uri = String.format(url, stringObjectMap.get(input));
//        }
//        String post = HttpUtil.post(uri, stringObjectMap);
//        //return BeanUtil.toBean(post, outputClass);
//        return ApiHandler.super.processInputs(inputs);
        //throw new UnsupportedOperationException("暂不支持");
        return inputs;
    }


    default <T> T processOutput(Object result, Class<T> outputClass) {
        //throw new UnsupportedOperationException("暂不支持");
        return BeanUtil.toBean(result, outputClass);
    }

    default <T> T execute(Object inputs, Class<T> outputClass) {
        throw new UnsupportedOperationException("暂不支持");
    }



   /* default <T> T execute(Object inputs, Class<T> outputClass) {
        ApiHandler apiHandler = null;
        try {
            apiHandler = SpringUtil.getBean(getName(), ApiHandler.class);
        } catch (Exception e) {
            //log.info("{}", e.getMessage());
        }
        // 简单http请求调用处理，调用当前自身方法即可完成
        if (apiHandler == null) {
            String url = this.getUrl();
            Map<String, Object> stringObjectMap = BeanUtil.beanToMap(inputs);
            String uri = this.getUrl();
            for (String input : getInputs()) {
                uri = String.format(uri, stringObjectMap.get(input));
            }
            String post = HttpUtil.post(uri, stringObjectMap);
            return BeanUtil.toBean(post, outputClass);
        } else {
            // 特定复杂逻辑处理，交由特定的处理器处理
            Object requestBody = apiHandler.processInputs(inputs);
            Object result = apiHandler.execute(requestBody);
            return apiHandler.processOutput(result, outputClass);
        }
    }*/
}
