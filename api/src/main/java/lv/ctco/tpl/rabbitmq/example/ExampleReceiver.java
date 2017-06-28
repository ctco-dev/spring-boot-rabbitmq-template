package lv.ctco.tpl.rabbitmq.example;

import lombok.extern.slf4j.Slf4j;
import lv.ctco.tpl.rabbitmq.configuration.RabbitMQConfiguration;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExampleReceiver {

    public static final String ID = "exampleReceiver";

    @RabbitListener(id = ID, bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = RabbitMQConfiguration.EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
                    key = ExampleRoutingKeys.EXAMPLE
            )
    })
    public void onMessage(String message) {
        log.info("Received message: {}", message);
    }
}
