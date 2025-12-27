package com.ssup.backend.domain.auth.oauth;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PathMatcher {

    private PathMatcher(){}

    public static final String[] SWAGGER = {
            "/", "/error", "/favicon.ico",
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
            "/swagger-resources", "/swagger-resources/**",
            "/api-docs/**", "/v3/api-docs/**", "/v3/api-docs/swagger-config/**",
    };

    public static final String[] AUTH = {
            "/oauth2/**", "/login/**", "/auth/**", "/signup/**",
            "/auth/success", "/login/oauth2/**", "/user/*/oauth2/info",
    };

    public static final String[] PUBLIC_GET = {
            "/posts/**", "/posts/*/comments/**", "/users/*/profile", "/comments/**"
    };

    public static final String[] OTHERS = {
            "/actuator/health", "/assets/**", "/actuator/health",
            "/h2-console/**", "/configuration/ui", "/configuration/security", "/webjars/**",
    };
}
