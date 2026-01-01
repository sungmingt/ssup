package com.ssup.backend.domain.auth.integration;

import com.ssup.backend.domain.auth.RefreshTokenRepository;
import com.ssup.backend.support.AuthTestApplication;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.UserStatus;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.infra.security.jwt.TokenInfo;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

//@SpringBootTest(classes = AuthTestApplication.class)
//@ActiveProfiles("auth-test")
////HTTP 서버 없이 실제 Spring MVC + Security 요청 흐름을 완전히 재현 (실제 HTTP 요청과 동일한 방식으로 MockMvc 를 통해 요청을 흘려보낸다)
//@AutoConfigureMockMvc
//class AuthIntegrationTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    RefreshTokenRepository refreshTokenRepository;
//
//    private User user;
//
//    @Autowired
//    private RedisTemplate<String, String> refreshTokenRedisTemplate;
//
//
//    @BeforeEach
//    void setUp() {
//        user = User.builder()
//                .email("test@test.com")
//                .password("1234")
//                .nickname("tester")
//                .status(UserStatus.ACTIVE)
//                .build();
//
//        userRepository.save(user);
//    }
//
//    @DisplayName("로그인-토큰 재발급-로그아웃 flow 테스트 - 성공")
//    @Test
//    void login_reissue_logout_flow_success() throws Exception {
//        //1. 로그인
//        MvcResult loginResult =
//                mockMvc.perform(post("/api/auth/login")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content("""
//                                    {"email":"test@test.com","password":"1234"}
//                                """))
//                        .andExpect(status().isNoContent())
//                        .andReturn();
//
//        Cookie refreshToken =
//                Arrays.stream(loginResult.getResponse().getCookies())
//                        .filter(c -> c.getName().equals(TokenInfo.REFRESH_TOKEN))
//                        .findFirst()
//                        .orElseThrow();
//
//        System.out.println("### RefreshTokenRepo Impl = " + refreshTokenRepository.getClass());
//        System.out.println("### refreshTokenCookie = " + refreshToken);
//        System.out.println("### saved RefreshToken = " + refreshTokenRepository.findByUserId(user.getId()));
//
//
//
//        String redisKey = "refreshToken:" + user.getId();
//        String rawValue = refreshTokenRedisTemplate.opsForValue().get(redisKey);
//        System.out.println("### Direct Redis Check "  + redisKey + " " + rawValue);
//
//
//        //2. 리프레시 토큰 재발급
//        MvcResult reissueResult =
//                mockMvc.perform(post("/api/auth/reissue")
//                                .cookie(refreshToken)
//                        )
//                        .andExpect(status().isOk())
//                        .andExpect(jsonPath("$.code").value(ErrorCode.TOKEN_REISSUED.name()))
//                        .andReturn();
//
//        Cookie newAccessToken =
//                Arrays.stream(reissueResult.getResponse().getCookies())
//                        .filter(c -> c.getName().equals(TokenInfo.ACCESS_TOKEN))
//                        .findFirst()
//                        .orElseThrow();
//
//        //3. 인증 확인
//        mockMvc.perform(get("/api/auth/me")
//                        .cookie(newAccessToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.nickname").value(user.getNickname()));
//
//        //4. 로그아웃
//        mockMvc.perform(post("/api/auth/logout")
//                        .cookie(refreshToken))
//                .andExpect(status().isNoContent());
//
//        //5. 로그아웃 이후 -> 재발급 실패
//        mockMvc.perform(post("/api/auth/reissue")
//                        .cookie(refreshToken))
//                .andExpect(status().isUnauthorized());
//    }
//}
