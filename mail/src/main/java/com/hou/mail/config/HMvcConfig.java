package com.hou.mail.config;

import com.hou.mail.component.HLocaleResolver;
import com.hou.mail.component.HLoginHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class HMvcConfig implements WebMvcConfigurer {
    //    @Bean
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/test").setViewName("success");
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/index").setViewName("login");
        registry.addViewController("/main").setViewName("component");
//        registry.addViewController("/main").setViewName("dashboard");
//        registry.addViewController("/main").setViewName("success");
        registry.addViewController("/fail-log").setViewName("login");
//        registry.addViewController("/")
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/templates/");
        registry.addResourceHandler("/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");
    }

    //注册拦截器

    /**
     * in sb，you need to put all son of static doc 拦截器的放行中，否则爆炸
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HLoginHandlerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/user-login",
                        "/register",
                        "/index",
                        "/login",
                        "/bootStrap/**",
                        "/myStrap/**",
                        "/user-register");
    }

    //    将所有组件注册在容器中
    @Bean
    public WebMvcConfigurer getWebMvcConfigurer() {
        return new HMvcConfig();
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new HLocaleResolver();
    }
}