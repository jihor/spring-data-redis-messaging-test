package ru.rgs.rnd.messaging.backend.config

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate
import ru.rgs.rnd.messaging.datamodel.Request
import ru.rgs.rnd.messaging.datamodel.Response

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
    RedisTemplate<String, Response> redisTemplate

    @Bean
    MessageListener responseListener() {
        new MessageListener() {
            @Override
            void onMessage(Message message, byte[] pattern) {
                def req = new ObjectInputStream(new ByteArrayInputStream(message.body)).readObject() as Request
                log.info "Received request: [$req] of class ${req.class.simpleName}"
                String key = req.firstName

                Response resp = Response.builder().errorString('0').response("Hello there $key".toString()).build()
                redisTemplate.opsForValue().set(key, resp, 1, TimeUnit.MINUTES)
            }
        }
    }
}
