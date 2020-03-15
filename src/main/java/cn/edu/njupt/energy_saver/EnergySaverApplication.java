package cn.edu.njupt.energy_saver;

import cn.edu.njupt.energy_saver.dataobject.UserControl;
import cn.edu.njupt.energy_saver.exception.CustomError;
import cn.edu.njupt.energy_saver.exception.LocalRuntimeException;
import cn.edu.njupt.energy_saver.service.UserService;
import cn.edu.njupt.energy_saver.util.JsonWrapper;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;


@SpringBootApplication
public class EnergySaverApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnergySaverApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer configurer(JsonWrapper jsonWrapper,
                                       HandlerMethodArgumentResolver userInfoArgumentResolver,
                                       HandlerInterceptor authInterceptor){
        return new WebMvcConfigurerAdapter() {
            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(jsonWrapper);
                super.configureMessageConverters(converters);
            }

            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
                argumentResolvers.add(userInfoArgumentResolver);
                super.addArgumentResolvers(argumentResolvers);
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/error");
                super.addInterceptors(registry);
            }
        };
    }

    @Configuration
    public static class UserInfoValidationConfigurer implements InitializingBean {

        private static final String COOKIE_KEY = "auth";

        @Autowired
        private UserService userService;

        public static ThreadLocal<UserControl> threadLocal = new ThreadLocal<>();

        @Bean
        public HandlerInterceptor authInterceptor(){
            return new HandlerInterceptorAdapter() {
                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                    String uri = request.getRequestURI();
                    Cookie cookie = getCookie(request, COOKIE_KEY);

                    if (uri.equals("/user/login")) {
                        UserControl userControl = userService.login(
                                request.getParameter("username"),
                                request.getParameter("password"));
                        Cookie c = new Cookie(COOKIE_KEY, userControl.getAuthId());
                        c.setPath("/");
                        c.setDomain("localhost");
                        response.addCookie(c);
//                        response.setHeader("set-cookie", COOKIE_KEY + "=" + userControl.getAuthId());
//                        response.getWriter().write(JSON.toJSONString(userControl));

                    } else if (cookie == null) {
                        throw new LocalRuntimeException(CustomError.UNAUTHORIZED);
                    } else {
                        UserControl userControl = userService.findUserByAuth(cookie.getValue());
                        if (userControl != null)
                            threadLocal.set(userControl);
                        else throw new LocalRuntimeException(CustomError.UNAUTHORIZED);
                    }

                    return true;
                }
            };

        }

        @Bean
        public HandlerMethodArgumentResolver userInfoArgumentResolver() {
            return new HandlerMethodArgumentResolver() {
                @Override
                public boolean supportsParameter(MethodParameter parameter) {
                    return true;
                }

                @Override
                public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
                    return threadLocal.get();
                }
            };
        }

        private static Cookie getCookie(HttpServletRequest request, String name) {
            Cookie[] cookies = request.getCookies();
            if (cookies == null || cookies.length == 0 || StringUtils.isEmpty(name)) {
                return null;
            }

            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
            return null;
        }
        @Override
        public void afterPropertiesSet() throws Exception {
            threadLocal.remove();
        }
    }
}
