//package org.dows.aac.config;
//
//import cn.hutool.core.collection.CollectionUtil;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.dows.aac.api.Auth;
//import org.dows.rbac.api.RbacApi;
//import org.dows.rbac.api.Resources;
//import org.dows.rbac.api.UriResource;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
//import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
//
//import java.lang.reflect.Method;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @description: </br>
// * @author: lait.zhang@gmail.com
// * @date: 3/18/2024 6:19 PM
// * @history: </br>
// * <author>      <time>      <version>    <desc>
// * 修改人姓名      修改时间        版本号       描述
// */
//@RequiredArgsConstructor
//@Component
//public class AacStartup implements ApplicationRunner {
//    private final RequestMappingInfoHandlerMapping requestMappingHandlerMapping;
//    private final AacConfig aacConfig;
//    private final RbacApi rbacApi;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        // 扫描并获取所有需要权限处理的接口资源(该方法逻辑写在下面)
//        List<Resources> resources = getAuthResources();
//        // 如果权限资源为空，就不用走后续数据插入步骤
//        if (CollectionUtil.isEmpty(resources)) {
//            return;
//        }
//        // 将资源数据批量添加到数据库
//        rbacApi.saveResource(resources);
//    }
//
//    /**
//     * 扫描并返回所有需要权限处理的接口资源
//     */
//    @Bean("resources")
//    private List<Resources> getAuthResources() {
//        // 接下来要添加到数据库的资源
//        List<Resources> list = new LinkedList<>();
//        // 拿到所有接口信息，并开始遍历
//        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
//        List<String> uriPackages = aacConfig.getUriPackages();
//        handlerMethods.forEach((info, handlerMethod) -> {
//            boolean matched = false;
//            for (String uriPackage : uriPackages) {
//                if (handlerMethod.getBeanType().getPackageName().startsWith(uriPackage)) {
//                    matched = true;
//                    break;
//                }
//            }
//            if (!matched) {
//                return;
//            }
//            // 拿到类(模块)上的权限注解（可填可不填）
//            Class<?> beanType = handlerMethod.getBeanType();
//            Auth moduleAuth = beanType.getAnnotation(Auth.class);
//            // 拿到接口方法上的权限注解
//            Method method = handlerMethod.getMethod();
//            Auth methodAuth = method.getAnnotation(Auth.class);
//            Operation operation = method.getAnnotation(Operation.class);
//            String name;
//            // package.class.method or classAuth.code.methodAuth.code
//            String code;
//            if (operation != null) {
//                name = operation.summary();
//            } else {
//                name = method.getName();
//            }
//            if (moduleAuth != null) {
//                code = moduleAuth.code();
//            } else {
//                code = beanType.getName();
//            }
//
//            if (methodAuth != null) {
//                code = code + "." + methodAuth.code();
//            } else {
//                code = code + "." + method.getName();
//            }
//
//            // 拿到该接口方法的请求方式(GET、POST等)
//            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
//            // 如果一个接口方法标记了多个请求方式，权限id是无法识别的，不进行处理
//            if (methods.size() != 1) {
//                return;
//            }
//            // 将请求方式和路径用`:`拼接起来，以区分接口。比如：GET:/user/{id}、POST:/user/{id}
//            String httpMethod = methods.toArray()[0].toString();
//            String path = httpMethod + ":" + info.getPathPatternsCondition().getPatterns().toArray()[0];
//            // 将权限名、资源路径、资源类型组装成资源对象，并添加集合中
//            UriResource resource = UriResource.builder()
//                    .path(path)
//                    .name(name)
//                    .code(code)
//                    .method(httpMethod)
//                    //.id() // 用code 代替确保唯一性
//                    .build();
//            list.add(resource);
//        });
//        return list;
//    }
//}
