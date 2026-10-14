package com.example.informationsystemslab1.config;

import com.example.informationsystemslab1.access.DirectAccessGuardInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final DirectAccessGuardInterceptor directAccessGuardInterceptor;

    public WebConfig(DirectAccessGuardInterceptor directAccessGuardInterceptor) {
        this.directAccessGuardInterceptor = directAccessGuardInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(directAccessGuardInterceptor);
    }
}
