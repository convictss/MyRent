package com.convict.configuration;

import com.convict.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Convict
 * @Date 2019/3/12 11:19
 */
@Configuration
public class LoginConfiguration implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        InterceptorRegistration ir = registry.addInterceptor(loginInterceptor);
        // 拦截路径
        ir.addPathPatterns("/**");
        // 排除路径
        List<String> excPath = new ArrayList<>();
        excPath.add("/");
        excPath.add("/static/**");
        excPath.add("/show/other/login");
        excPath.add("/show/captcha");
        excPath.add("/manage/login");
        // 清除errorTip用
        excPath.add("/manage/removeSessionByKey");
        ir.excludePathPatterns(excPath);
    }
}
