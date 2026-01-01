package com.ssup.backend.support;

import com.ssup.backend.domain.auth.AuthController;
import com.ssup.backend.domain.auth.AuthService;
import com.ssup.backend.domain.auth.RefreshTokenRepository;
import com.ssup.backend.global.config.RedisConfig;
import com.ssup.backend.infra.security.jwt.JwtCookieProvider;
import com.ssup.backend.infra.security.jwt.JwtProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {
        AuthController.class,
        AuthService.class,
        JwtProvider.class,
        JwtCookieProvider.class,
        RefreshTokenRepository.class,
        RedisConfig.class
})
@EntityScan(basePackages = "com.ssup.backend.domain")
@EnableJpaRepositories(basePackages = "com.ssup.backend.domain")
@Import(TestSecurityConfig.class)
public class AuthTestApplication {

    //인증 테스트에 필요한 클래스들만 스캔하도록.
    //운영 Spring Context 를 그대로 띄우지 않고, 인증 테스트에 필요한 최소한의 구성만 조립

    //이 테스트의 목적은 '인증이 정상적으로 동작하는가' 인데, Redis/S3/OAuth2/Scheduler 등 전부 주입 -> 무거워지고 비효율적
//
//    @Bean
//    @Primary
//    public RefreshTokenRepository refreshTokenRepository() {
//        return new TestRefreshTokenRepository();
//    }
}