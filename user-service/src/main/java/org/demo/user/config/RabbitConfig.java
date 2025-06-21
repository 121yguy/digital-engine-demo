package org.demo.user.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RabbitConfig {

    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            log.error("消息路由失败");
            log.debug("Exchange: {}", returnedMessage.getExchange());
            log.debug("RoutingKey: {}", returnedMessage.getRoutingKey());
            log.debug("Message: {}", returnedMessage.getMessage());
            log.debug("ReplyCode: {}", returnedMessage.getReplyCode());
            log.debug("ReplyText: {}", returnedMessage.getReplyText());
        });
    }

}
