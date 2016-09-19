package ru.rgs.rnd.messaging.frontend.config

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rgs.rnd.messaging.datamodel.Request
import ru.rgs.rnd.messaging.datamodel.Response

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2016-09-12
 */
@RestController
@Slf4j
@Configuration
class RestInterfaceConfiguration {
    @Autowired
    RedisTemplate<String, Request> redisReqTemplate

    @Autowired
    RedisTemplate<String, Response> redisRespTemplate

    @RequestMapping("/hello-redis/{name}")
    String helloRedis(@PathVariable String name) {
        String result

        result = getCached(name)
        if (result) {
            return 'Got a cached response: ' + result.toString()
        }

        Request req = Request.builder().firstName(name).lastName("fake").build()
        redisReqTemplate.convertAndSend(MainConfiguration.REQUEST_TOPIC, req)

        int i = 0
        while (i++ < 100 && (result = getCached(name)) == null) {
            sleep 100
        }
        return result?.toString() ?: "Timed out waiting for response :("
    }

    private Response getCached(String name) {
        redisRespTemplate.opsForValue().get(name)
    }
}
