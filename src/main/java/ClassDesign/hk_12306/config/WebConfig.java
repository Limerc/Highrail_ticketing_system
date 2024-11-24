package ClassDesign.hk_12306.config;

import ClassDesign.hk_12306.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry注册拦截器，会拦截所有请求；后面放行登录和注册的请求
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/loginByPhone","/user/loginByID","/user/register");
    }
}
