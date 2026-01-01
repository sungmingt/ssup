package com.ssup.backend.infra.security.jwt;

public class TokenInfo {

    private TokenInfo(){}

    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";

    public static final long ACCESS_TOKEN_TTL_MILLISECONDS = 1000L * 60 * 30;
    public static final long REFRESH_TOKEN_TTL_MILLISECONDS = 1000L * 60 * 60 * 24;
}
