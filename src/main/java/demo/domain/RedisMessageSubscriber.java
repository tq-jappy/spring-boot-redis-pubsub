package demo.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class RedisMessageSubscriber implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(RedisMessageSubscriber.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.debug("receive message : {}", new String(message.getBody(), StandardCharsets.UTF_8));

        // do stuff
    }
}
