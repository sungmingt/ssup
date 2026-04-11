package com.ssup.backend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssup.backend.domain.user.recommend.UserProfileEmbedding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.token.host}")
    private String host;

    @Value("${spring.redis.token.port}")
    private int port;

//    @Primary
    @Bean
    public StringRedisTemplate refreshTokenRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(tokenRedisConnectionFactory());
        template.setEnableTransactionSupport(false);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, UserProfileEmbedding> embeddingRedisTemplate(ObjectMapper objectMapper) {
        RedisTemplate<String, UserProfileEmbedding> template = new RedisTemplate<>();

        template.setConnectionFactory(tokenRedisConnectionFactory());

        Jackson2JsonRedisSerializer<UserProfileEmbedding> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, UserProfileEmbedding.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, List<Long>> recommendRedisTemplate() {
        RedisTemplate<String, List<Long>> template = new RedisTemplate<>();
        template.setConnectionFactory(tokenRedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(List.class));
        template.afterPropertiesSet();
//        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisConnectionFactory tokenRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
}
