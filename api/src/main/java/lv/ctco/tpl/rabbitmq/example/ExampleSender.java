package lv.ctco.tpl.rabbitmq.example;

import lv.ctco.tpl.rabbitmq.configuration.RabbitMQConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExampleSender {

    @Autowired
    RabbitTemplate template;

    public void send(String message) {
        template.convertAndSend(RabbitMQConfiguration.QUEUE_NAME, message);
    }
}
