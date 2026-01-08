package com.ssup.backend.domain.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RefreshTokenRepositoryTest {

    @InjectMocks
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;


    @Test
    @DisplayName("refreshToken 저장 - 성공")
    void save_success() {
        //given
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        //when
        refreshTokenRepository.save(1L, "rtk");

        //then
        verify(valueOperations).set(
                eq("refreshToken:1"),
                eq("rtk"),
                any(Duration.class)
        );
    }

    @Test
    @DisplayName("refreshToken 조회 - 성공")
    void find_success() {
        //given
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("refreshToken:1")).willReturn("rtk");

        //when
        Optional<String> result = refreshTokenRepository.findByUserId(1L);

        //then
        assertThat(result).contains("rtk");
    }

    @Test
    @DisplayName("refreshToken 조회 실패 - 존재하지 않는 토큰 id")
    void find_fail_notFound() {
        //given
        given(valueOperations.get("refreshToken:1")).willReturn(null);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        //when
        Optional<String> result = refreshTokenRepository.findByUserId(1L);

        //then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("refreshToken 삭제 - 성공")
    void delete_success() {
        //when
        refreshTokenRepository.deleteById(1L);

        //then
        verify(redisTemplate).delete("refreshToken:1");
    }
}
