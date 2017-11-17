package demo.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(RedisMessagePublisher.class);

    private final RedisTemplate<String, EventMessage> redisTemplate;

    private final ChannelTopic topic;

    public RedisMessagePublisher(@Qualifier("eventMessageRedisTemplate") RedisTemplate<String, EventMessage> redisTemplate,
                                 ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(String message) {
        log.debug("publish message : {}", message);

        redisTemplate.convertAndSend(topic.getTopic(), new EventMessage(message));
    }
}
