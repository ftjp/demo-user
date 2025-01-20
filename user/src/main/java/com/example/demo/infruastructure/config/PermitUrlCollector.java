package com.example.demo.infruastructure.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LJP
 * @date 2024/11/26 16:05
 */
@Slf4j
@Component
public class PermitUrlCollector {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private List<String> permitAllUrls = new ArrayList<>();

    public PermitUrlCollector(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @PostConstruct
    public void collectPermitAllUrls() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            Method method = handlerMethod.getMethod();
            try {
                if (method.isAnnotationPresent(Permit.class) && method.getAnnotation(Permit.class).value()) {
                    RequestMappingInfo requestMappingInfo = entry.getKey();
                    Set<String> patterns = requestMappingInfo.getPathPatternsCondition().getPatterns().stream().map(PathPattern::getPatternString).collect(Collectors.toSet());
                    patterns.forEach(url -> permitAllUrls.add(url));
                }
            } catch (Exception e) {
                log.info("method: {}; 获取url失败:{}", method, e);
            }

        }
    }

    public List<String> getPermitUrls() {
        return permitAllUrls;
    }
}