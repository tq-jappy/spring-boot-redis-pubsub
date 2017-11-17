package demo.domain;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

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

        return result;
    }
}
