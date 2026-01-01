package com.ssup.backend.fixture;

import com.ssup.backend.domain.auth.AppUser;
import com.ssup.backend.infra.security.jwt.JwtProvider;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("test")
public class TestAppUserFactory {

    public Authentication authentication() {
        AppUser user = new AppUser(1L);
        return new UsernamePasswordAuthenticationToken(user, null, List.of());
    }

    @Bean
    public JwtProvider jwtProvider() {
        return Mockito.mock(JwtProvider.class);
    }
}
