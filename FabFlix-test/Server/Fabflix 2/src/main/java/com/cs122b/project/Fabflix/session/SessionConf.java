package com.cs122b.project.Fabflix.session;

import com.cs122b.project.Fabflix.Response.BaseResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.RedirectViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
public class SessionConf implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SecurityInterceptor())
//                //排除拦截
//                .excludePathPatterns("/api/login")
//                .excludePathPatterns("/api/logout");

                //拦截路径
                //.addPathPatterns("/**");
    }

//    @Override
//    public void addViewControllers (ViewControllerRegistry registry) {
//
//        RedirectViewControllerRegistration r =
//                registry.addRedirectViewController("/**", "/api/login");
//    }


    //Any other URLs should be redirect to Login Page if not logged in yet!!!!!!!!!!!!!!!
    @Configuration
    public class SecurityInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            HttpSession session = request.getSession();
            return session.getAttribute(session.getId()) != null;
        }
    }
}