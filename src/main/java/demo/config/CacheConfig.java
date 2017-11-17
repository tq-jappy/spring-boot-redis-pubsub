package demo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @Primary
    public CacheManager redisCacheManager(RedisTemplate<String, Object> redisTemplate,
                                          RedisConnectionFactory redisConnectionFactory) {
        Map<String, Long> expires = new HashMap<>();
        expires.put("foo", Duration.ofMinutes(1).getSeconds());

        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setExpires(expires);
        return cacheManager;
    }

    @Bean("caffeineCache")
    public CacheManager caffeineCacheManager(Ticker ticker) {
        CaffeineCache cache1 = buildCache("bar", ticker, Duration.ofSeconds(30L));

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(cache1));
        return cacheManager;
    }

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }

    private CaffeineCache buildCache(String name, Ticker ticker, Duration durationToExpire) {
        return new CaffeineCache(name,
                Caffeine.newBuilder()
                        .expireAfterAccess(durationToExpire.getSeconds(), TimeUnit.SECONDS)
                        .maximumSize(100L)
                        .ticker(ticker)
                        .build());
    }
}
