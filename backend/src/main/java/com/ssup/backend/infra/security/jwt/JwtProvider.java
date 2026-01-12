package com.ssup.backend.infra.security.jwt;

import com.ssup.backend.global.exception.SsupException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.ssup.backend.infra.security.jwt.TokenInfo.*;
import static com.ssup.backend.global.exception.ErrorCode.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.key}")
    private String key;
    private SecretKey secretKey;

    @PostConstruct
    private void setSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Long userId, String sessionId) {
        return createToken(userId, sessionId, ACCESS_TOKEN_TTL_MILLISECONDS);
    }

    public String createRefreshToken(Long userId, String sessionId) {
        return createToken(userId, sessionId, REFRESH_TOKEN_TTL_MILLISECONDS);
    }

    private String createToken(Long userId, String sessionId, long timeToLive) {
        Date issueDate = new Date();
        Date expireDate = new Date(issueDate.getTime() + timeToLive);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("sessionId", sessionId)
                .issuedAt(issueDate)
                .expiration(expireDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    public String getSessionIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return String.valueOf(claims.get("sessionId"));
    }

    public TokenStatus validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return TokenStatus.VALID;
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            return TokenStatus.INVALID;
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean checkRefreshTokenSameness(String inputRefreshToken, String exRefreshToken) {
        return inputRefreshToken.equals(exRefreshToken);
    }

    public Long getTimeToLiveLeft(String accessToken) {
        Date expiration = parseClaims(accessToken).getExpiration();
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(
                claims.get("role").toString()));
    }

//    public Claims parseClaims(String token) {
//        try {
//            Objects.requireNonNull(token);
//            return Jwts.parser().verifyWith(secretKey).build()
//                    .parseSignedClaims(token).getPayload();
//        } catch (ExpiredJwtException e) {
//            throw new SsupException(TOKEN_EXPIRED);
//        } catch (MalformedJwtException e) {
//            throw new SsupException(INVALID_TOKEN);
//        } catch (SecurityException e) {
//            throw new SsupException(TOKEN_SIGNATURE_INVALID);
//        }
//    }
}