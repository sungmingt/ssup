package com.ssup.backend.global.config;

import com.ssup.backend.infra.security.oauth.CustomOAuth2UserService;
import com.ssup.backend.infra.security.jwt.JwtAuthenticationFilter;
import com.ssup.backend.infra.security.oauth.OAuth2SuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import static com.ssup.backend.infra.security.SecurityPaths.*;

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(name = "ssup.security.enabled", havingValue = "true", matchIfMissing = true)
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable) //기본 인증 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable) //Spring 기본 로그인 페이지 제거
                .logout(AbstractHttpConfigurer::disable) //세션 기반 로그아웃 엔드포인트 제거
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                //cors
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                //permit requests
                .authorizeHttpRequests(request -> request
                        //me
                        .requestMatchers("/api/users/me/**").authenticated()
                        .requestMatchers("/api/auth/me/**").authenticated()

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

                        .anyRequest().authenticated()
                )

                //oauth2
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )

                //filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(e -> e
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"message\":\"UNAUTHORIZED\"}");
                    }
                    )
                );

        return http.build();
    }

}
