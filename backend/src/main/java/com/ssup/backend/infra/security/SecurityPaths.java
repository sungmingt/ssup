package com.ssup.backend.infra.security;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityPaths {

    private SecurityPaths(){}

    public static final String[] SWAGGER = {
            "/", "/error", "/favicon.ico",
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
            "/swagger-resources", "/swagger-resources/**",
            "/api-docs/**", "/v3/api-docs/**", "/v3/api-docs/swagger-config/**",
    };

    public static final String[] AUTH = {
            "/api/auth/reissue", "/api/auth/login/**",  "/api/auth/signup/**",
            "/api/auth/logout", "/login/**", "/auth/success",
            "/login/oauth2/**", "/user/*/oauth2/info", "/oauth2/**",
    };

    public static final String[] PUBLIC_GET = {
            //posts, comments
            "/api/posts/**", "/api/posts/*/comments/**", "/api/comments/**",

            //user
            "/api/users/*/languages", "/api/users/*/profile",

            //language, location, interest
            "/api/languages/**", "/api/locations/**", "/api/interests/**"
    };

    public static final String[] OTHERS = {
            "/actuator/health", "/assets/**", "/actuator/health",
            "/h2-console/**", "/configuration/ui", "/configuration/security", "/webjars/**",
    };
}
