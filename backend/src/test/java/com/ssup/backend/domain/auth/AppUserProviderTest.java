package com.ssup.backend.domain.auth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AppUserProviderTest {

    private final SecurityAppUserProvider provider = new SecurityAppUserProvider();

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("null - 인증되지 않은 유저")
    void getUserId_notAuthenticated() {
        //given
        SecurityContextHolder.clearContext();

        //when, then
        assertThat(provider.getUserId()).isNull();
    }

    @Test
    @DisplayName("null - 외부 유저")
    void getUserId_anonymous() {
        //given
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "anonymousUser", null, List.of()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        //when, then
        assertThat(provider.getUserId()).isNull();
    }

    @Test
    @DisplayName("id 존재 - 인증된 유저")
    void getUserId_authenticated() {
        //given
        AppUser appUser = new AppUser(1L);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                appUser, null, List.of()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        //when, then
        assertThat(provider.getUserId()).isEqualTo(1L);
    }
}