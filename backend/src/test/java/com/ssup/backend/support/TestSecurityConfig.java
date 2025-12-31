package com.ssup.backend.support;

import com.ssup.backend.infra.security.jwt.JwtAuthenticationFilter;
import com.ssup.backend.infra.security.jwt.JwtCookieProvider;
import com.ssup.backend.infra.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.ssup.backend.infra.security.SecurityPaths.*;
import static com.ssup.backend.infra.security.SecurityPaths.OTHERS;

@TestConfiguration
@Profile("auth-test")
public class TestSecurityConfig {

    //운영 보안 구조를 깨지 않고 인증 파이프라인(JWT → SecurityContext)만 검증하기 위해 생성
    //@TestConfiguration은 자동으로 스캔되지 않기 때문에, @Import를 사용해 명시적으로 불러와야 함

    @Bean
    SecurityFilterChain testFilterChain(HttpSecurity http,
                                        JwtProvider jwtProvider,
                                        JwtCookieProvider cookieProvider) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtProvider, cookieProvider);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                //permit requests
                .authorizeHttpRequests(request -> request
                        //users
                        .requestMatchers("/api/users/me/**").authenticated()

                        //posts
                        .requestMatchers(HttpMethod.POST, "/api/posts/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/posts/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/**").authenticated()

                        //comments
                        .requestMatchers(HttpMethod.POST, "/api/posts/*/comments/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/posts/*/comments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/*/comments/**").authenticated()

                        //hearts
                        .requestMatchers(HttpMethod.POST, "/api/posts/*/hearts/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/comments/*/hearts/**").authenticated()


                        //permitAll
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()
                        .requestMatchers(SWAGGER).permitAll()
                        .requestMatchers(AUTH).permitAll()
                        .requestMatchers(OTHERS).permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/auth/reissue/**").permitAll()


                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");
                        })
                        .accessDeniedHandler((req, res, ex) -> {
                            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");
                        })
                )

                .build();
    }
}