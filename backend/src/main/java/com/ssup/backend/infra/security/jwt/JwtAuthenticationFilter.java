package com.ssup.backend.infra.security.jwt;

import com.ssup.backend.domain.auth.AppUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.ssup.backend.infra.security.jwt.TokenInfo.ACCESS_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtCookieProvider cookieProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        log.info("### filter 진입");
        String accessToken = cookieProvider.getTokenFromCookie(request, ACCESS_TOKEN).orElse(null);

        log.info("### ATK={}", accessToken);

        if (accessToken == null){
            SecurityContextHolder.clearContext();
            chain.doFilter(request,response);
            return;
        }

        TokenStatus status = jwtProvider.validateToken(accessToken);

        log.info("### ATK status={}", status);

        //ATK 만료 시, 무조건 재발급 시도하도록 -> UX 개선
        if (status == TokenStatus.EXPIRED) {
            //EntryPoint와 동일
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":\"TOKEN_EXPIRED\", \"message\":\"UNAUTHORIZED\"}");
            return;
        }

        if (status == TokenStatus.INVALID) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }

        log.info("### ATK 검증 통과");

        Long userId = jwtProvider.getUserIdFromToken(accessToken);
        AppUser appUser = new AppUser(userId);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        appUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();

        if (pathMatcher.match("/login/oauth2/**", uri)
                || pathMatcher.match("/oauth2/**", uri)
                || pathMatcher.match("/api/auth/login", uri)
                || pathMatcher.match("/api/auth/logout", uri)
                || pathMatcher.match("/api/auth/reissue", uri)
                || pathMatcher.match("/swagger-ui/**", uri)
                || pathMatcher.match("/v3/api-docs/**", uri)) {
            return true;
        }

        return false;
    }
}