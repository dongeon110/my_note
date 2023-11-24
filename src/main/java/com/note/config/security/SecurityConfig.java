package com.note.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 스프링의 환경설정 파일을 의미
//@EnableWebSecurity // 모든 요청 URL이 시큐리티의 제어를 받도록 하는 애너테이션
@EnableMethodSecurity
public class SecurityConfig {
    @Bean // 세부설정은 SecurityFilterChain 빈을 생성하여 설정
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(
                        (authorizeHttpRequests) -> authorizeHttpRequests
                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/v3/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/board/**")).permitAll()
                                .anyRequest().authenticated())
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers((new AntPathRequestMatcher("/board/api/**"))))
        ;


        return http.build();
    }
}
