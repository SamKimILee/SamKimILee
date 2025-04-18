package app.labs.register.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import app.labs.register.interceptor.LoginCheckInterceptor;
import app.labs.notice.interceptor.NoticeInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor // final 필드를 매개변수로 받는 생성자 자동 생성해줌
public class WebConfig implements WebMvcConfigurer {

    private final NoticeInterceptor noticeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/members/**")
                .excludePathPatterns("/login", "/logout", "/members/insert", "/members/check-memberid", "/members/find-username", "/members/find-password", "/members/check-memberNick"); // 인증 예외 경로 추가

        List<String> staticResourcesPath = Arrays.stream(StaticResourceLocation.values())
                .flatMap(StaticResourceLocation::getPatterns)
                .collect(Collectors.toList());
        staticResourcesPath.add("");

        registry.addInterceptor(noticeInterceptor)
                .excludePathPatterns(staticResourcesPath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:8080", "http://localhost:8080") // 허용할 출처 : 특정 도메인만 받을 수 있음
                .allowedMethods("GET", "POST") // 허용할 HTTP method
                .allowCredentials(true); // 쿠키 인증 요청 허용
    }
}
