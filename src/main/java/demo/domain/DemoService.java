package demo.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    private static final Logger log = LoggerFactory.getLogger(DemoService.class);

    private final CacheManager redisCacheManager;

    private final CacheManager caffeineCacheManager;

    private final RedisMessagePublisher redisMessagePublisher;

    public DemoService(CacheManager redisCacheManager,
                       CacheManager caffeineCacheManager,
                       RedisMessagePublisher redisMessagePublisher) {
        this.redisCacheManager = redisCacheManager;
        this.caffeineCacheManager = caffeineCacheManager;
        this.redisMessagePublisher = redisMessagePublisher;
    }

    @Cacheable(value = "foo", key = "#q")
    public String getFoo(String q) {
        String result = q.toUpperCase();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException ignored) {
        }

        return result;
    }

    @Cacheable(value = "bar", key = "#q", cacheManager = "caffeineCache")
    public String getBar(String q) {
        String result = q.toLowerCase();

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException ignored) {
        }

        redisMessagePublisher.publish("publish : " + q + " -> " + result);

        return result;
    }

    public void clearCache() {
        log.debug("clear cache.");

        redisCacheManager.getCache("foo").clear();
        caffeineCacheManager.getCache("bar").clear();
    }
}
