package ru.rgs.rnd.messaging.frontend.config

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2016-09-12
 */
@RestController
@Slf4j
@Configuration
class RestInterfaceConfiguration {
    @Autowired
    StringRedisTemplate redisTemplate

    @RequestMapping("/hello-redis/{name}")
    String helloRedis(@PathVariable String name){
        String key = name
        String result
        result = redisTemplate.opsForHash().get(key, "response")
        if (result != null) {
            return 'Got a cached response: ' + result.toString()
        }
        redisTemplate.convertAndSend(MainConfiguration.REQUEST_TOPIC, name)
        int i = 0
        while (i++ < 100){
            result = redisTemplate.opsForHash().get(key, "response")
            if (result != null) {
                return result.toString()
            } else {
                sleep 100
            }
        }
        return "Timed out waiting for response :("
    }
}
