package com.ssup.backend.support;

import com.ssup.backend.domain.auth.RefreshTokenRepository;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

//@Primary
//@TestComponent
//public class TestRefreshTokenRepository extends RefreshTokenRepository {
//
//    //실제 RedisTemplate을 띄우지 않고, 테스트만을 위한 컴포넌트
//
//    private final Map<String, String> store = new ConcurrentHashMap<>();
//
//    public TestRefreshTokenRepository() {
//        super(null);
//    }
//
//    @Override
//    public void save(Long userId, String refreshToken) {
//        store.put(String.valueOf(userId), refreshToken);
//    }
//
//    @Override
//    public Optional<String> findByUserId(Long userId) {
//        return Optional.ofNullable(store.get(String.valueOf(userId)));
//    }
//
//    @Override
//    public void deleteById(Long userId) {
//        store.remove(String.valueOf(userId));
//    }
//}