package lv.ctco.tpl.rabbitmq.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageReceiver {

    public void onMessage(Object message) {
        log.info("Message received: {}", message);
    }
}
