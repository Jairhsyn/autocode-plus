package tech.washmore.autocodeplus.common.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * @author Washmore
 * @version V1.0
 * @summary MVC相关配置
 * @Copyright (c) 2018, Washmore All Rights Reserved.
 * @since 2018/1/15
 */
@Configuration
@ConditionalOnWebApplication
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * @summary 静态资源处理器
     * @version V1.0
     * @author Washmore
     * @since 2018/1/15
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        CacheControl cacheControl = CacheControl.maxAge(8, TimeUnit.HOURS);
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/pages/static/").setCacheControl(cacheControl);
//        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/images/favicon.ico").setCachePeriod(0);
    }

}
