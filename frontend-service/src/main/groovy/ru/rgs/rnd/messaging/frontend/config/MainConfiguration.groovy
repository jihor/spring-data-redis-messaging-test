package ru.rgs.rnd.messaging.frontend.config

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.RestController
/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 *         (С) RGS Group, http://www.rgs.ru
 *         Created on 2016-07-06
 */
@RestController
@Slf4j
@Import([RestInterfaceConfiguration])
class MainConfiguration {
    public static final String REQUEST_TOPIC = "request-topic"

    @Value('${redis.host:redis}')
    private String redisHost


    @Value('${redis.port:6379}')
    private Integer redisPort

    @Bean
    RedisConnectionFactory connectionFactory(){
        def connectionFactory = new JedisConnectionFactory()
        connectionFactory.hostName = redisHost
        connectionFactory.port = redisPort
        connectionFactory
    }

    @Bean
    RedisTemplate redisTemplate(RedisConnectionFactory cf){
        def template = new RedisTemplate()
        template.setConnectionFactory(cf)
        template.afterPropertiesSet()
        template
    }
}
