package demo.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RedisMessagePubSubTest {

    @Autowired
    RedisMessagePublisher publisher;

    static TestSubscriber subscriber = new TestSubscriber();

    @TestConfiguration
    static class TestConfig {

        @Bean
        public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                            Topic topic) {
            MessageListenerAdapter messageListener = new MessageListenerAdapter(subscriber);
            messageListener.setSerializer(new Jackson2JsonRedisSerializer<>(EventMessage.class));
            messageListener.afterPropertiesSet();

            RedisMessageListenerContainer container = new RedisMessageListenerContainer();
            container.setConnectionFactory(connectionFactory);
            container.addMessageListener(messageListener, topic);
            return container;
        }
    }

    static class TestSubscriber {

        List<EventMessage> receivedMessages = new ArrayList<>();

        public void handleMessage(EventMessage message) {
            receivedMessages.add(message);
        }

        Optional<String> lastMessage() {
            if (receivedMessages.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(receivedMessages.get(receivedMessages.size() - 1).getText());
        }
    }

    @Test
    void testPubSub() throws Exception {
        publisher.publish("first message");
        Thread.sleep(100L);

        assertEquals("first message", subscriber.lastMessage().orElse("Not Found"));

        publisher.publish("second message");
        Thread.sleep(100L);

        assertEquals("second message", subscriber.lastMessage().orElse("Not Found"));
    }
}
