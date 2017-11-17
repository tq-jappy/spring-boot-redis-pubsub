package demo.config;

import demo.domain.EventMessage;
import demo.domain.RedisMessageSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean("eventMessageRedisTemplate")
    public RedisTemplate<String, EventMessage> eventMessageRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, EventMessage> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(EventMessage.class));
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public MessageListenerAdapter messageListener(RedisMessageSubscriber redisMessageSubscriber) {
        MessageListenerAdapter messageListenerAdapter =
                new MessageListenerAdapter(redisMessageSubscriber, MessageListenerAdapter.ORIGINAL_DEFAULT_LISTENER_METHOD);
        messageListenerAdapter.setSerializer(new Jackson2JsonRedisSerializer<>(EventMessage.class));
        return messageListenerAdapter;
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("messageQueue");
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                        MessageListenerAdapter messageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListener, topic());
        return container;
    }
}
