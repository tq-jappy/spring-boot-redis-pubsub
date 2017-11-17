package demo.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber {

    private static final Logger log = LoggerFactory.getLogger(RedisMessageSubscriber.class);

    public void handleMessage(EventMessage message) {
        log.debug("receive message : {}", message);

        // do stuff
    }
}
