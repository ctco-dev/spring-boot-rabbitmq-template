package lv.ctco.tpl.rabbitmq.example;

import lombok.extern.slf4j.Slf4j;
import lv.ctco.tpl.rabbitmq.configuration.RabbitMQConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExampleReceiver {

    public static final String ID = "exampleReceiver";

    @RabbitListener(id = ID, queues = RabbitMQConfiguration.QUEUE_NAME)
    public void onMessage(String message) {
        log.info("Received message: {}", message);
    }
}
