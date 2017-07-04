package lv.ctco.tpl.rabbitmq.configuration.sleuth;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SleuthAMQPMessagePostProcessor implements MessagePostProcessor {

    @Autowired
    SleuthMessageBridge bridge;

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        bridge.populateHeaders(message);
        return message;
    }
}
