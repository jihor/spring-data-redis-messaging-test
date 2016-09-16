package ru.rgs.rnd.messaging.backend.config

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate

import javax.annotation.PostConstruct
import java.util.concurrent.TimeUnit
/**
 *
 *
 * @author jihor (jihor@ya.ru)
 * Created on 2016-09-12
 */
@Slf4j
class ListenerConfiguration {
    @Autowired
    RedisTemplate redisTemplate

    @Bean
    MessageListener responseListener(){
        new MessageListener() {
            @Override
            void onMessage(Message message, byte[] pattern) {
                log.info "Received request: $message"
                String response = "Hello there $message".toString()
                String key = message.toString()
                redisTemplate.opsForHash().put(key, "response", response)
                redisTemplate.expire(key, 1, TimeUnit.MINUTES)
            }
        }
    }
}
