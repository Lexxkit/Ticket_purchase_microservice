package com.lexxkit.stmmicroservices.ticketpurchase.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
  @Value("${redis.host}")
  private String redisHost;

  @Value("${redis.port}")
  private Integer redisPort;

  private final ObjectMapper objectMapper;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);

    return new LettuceConnectionFactory(configuration);
  }

  @Bean
  public RedisCacheManager cacheManager() {
    RedisCacheConfiguration cacheConfig = RedisCacheConfiguration
        .defaultCacheConfig()
        .serializeValuesWith(
            SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));

    return RedisCacheManager.builder(redisConnectionFactory())
        .cacheDefaults(cacheConfig)
        .build();
  }
}
