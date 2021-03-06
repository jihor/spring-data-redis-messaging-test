package ru.rgs.rnd.messaging.backend.config

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.web.bind.annotation.RestController
/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 *         (С) RGS Group, http://www.rgs.ru
 *         Created on 2016-07-06
 */
@RestController
@Slf4j
@Import(ListenerConfiguration)
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
    RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory cf,
            MessageListenerAdapter listenerAdapter) {
        def container = new RedisMessageListenerContainer()
        container.setConnectionFactory(cf)
        container.addMessageListener(listenerAdapter, new PatternTopic(REQUEST_TOPIC))
        container
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MessageListener listener) {
        new MessageListenerAdapter(listener)

    }

    @Bean
    RedisTemplate redisTemplate(RedisConnectionFactory cf){
        def template = new RedisTemplate()
        template.setConnectionFactory(cf)
        template.afterPropertiesSet()
        template
    }
}
