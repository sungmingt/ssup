package com.ssup.backend.support;

import com.ssup.backend.infra.security.jwt.JwtAuthenticationFilter;
import com.ssup.backend.infra.security.jwt.JwtCookieProvider;
import com.ssup.backend.infra.security.jwt.JwtProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@TestConfiguration
@Profile("auth-test")
public class TestSecurityConfig {

    //운영 보안 구조를 깨지 않고 인증 파이프라인(JWT → SecurityContext)만 검증하기 위해 생성
    //@TestConfiguration은 자동으로 스캔되지 않기 때문에, @Import를 사용해 명시적으로 불러와야 함

    @Bean
    SecurityFilterChain testFilterChain(HttpSecurity http,
                                        JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtProvider jwtProvider,
                                                           JwtCookieProvider cookieProvider) {
        return new JwtAuthenticationFilter(jwtProvider, cookieProvider);
    }
}