package com.ssup.backend.global.config;

import com.ssup.backend.infra.security.jwt.JwtCookieProvider;
import com.ssup.backend.infra.security.jwt.JwtProvider;
import com.ssup.backend.infra.security.oauth.CustomOAuth2UserService;
import com.ssup.backend.infra.security.jwt.JwtAuthenticationFilter;
import com.ssup.backend.infra.security.oauth.OAuth2SuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
    private final JwtProvider jwtProvider;
    private final JwtCookieProvider cookieProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtProvider, cookieProvider);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable) //기본 인증 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable) //Spring 기본 로그인 페이지 제거
                .logout(AbstractHttpConfigurer::disable) //세션 기반 로그아웃 엔드포인트 제거
                //oauth2 인증 과정에서, HttpSession 에 저장된 authorizationRequest 조회하기 때문에 open 해야함.
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
//                .securityContext(sc -> sc.requireExplicitSave(false))

                //cors
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

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

                //oauth2
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"message\":\"OAUTH2_LOGIN_FAILED\"}");
                        })
                )

                //filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"message\":\"UNAUTHORIZED\"}");
                        })
                        .accessDeniedHandler((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.getWriter().write("{\"message\":\"UNAUTHORIZED\"}");
                            res.getWriter().flush();
                        })

                );

        return http.build();
    }
}
