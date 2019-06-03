package com.convict.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Convict
 * @Date 2019/3/1 15:20
 */

/**
 * 默认页页，默认static/index.html > templates/index.html
 * 可以手动配置，会覆盖默认设置
 */
@Configuration
public class DefaultViewConfiguration implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
//        registry.addViewController("/").setViewName("other/catalog");
//        registry.addViewController("/").setViewName("login");
    }
}
