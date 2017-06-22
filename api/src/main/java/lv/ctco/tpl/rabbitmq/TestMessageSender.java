package lv.ctco.tpl.rabbitmq;

import lv.ctco.tpl.rabbitmq.configuration.RabbitMQConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TestMessageSender {

    @Autowired
    RabbitTemplate template;

    @PostConstruct
    public void init() {
        template.convertAndSend(RabbitMQConfiguration.QUEUE_NAME, "hello, world");
    }
}
