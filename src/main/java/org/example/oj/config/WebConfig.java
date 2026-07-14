package org.example.oj.config;

import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor  tokenInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/register", "/user/getByUsername",
                        "/problem/list", "/problem/{id:\\d+}",  // 题目列表和详情公开
                        "/login.html", "/problems.html", "/problem.html", "/create_problem.html",
                        "/**.html", "/**.js", "/**.css"); // 静态页面不拦截
    }
}
