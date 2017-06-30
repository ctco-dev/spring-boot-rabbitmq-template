package lv.ctco.tpl.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import lv.ctco.tpl.rabbitmq.example.ExampleBean;
import lv.ctco.tpl.rabbitmq.example.ExampleObjectReceiver;
import lv.ctco.tpl.rabbitmq.example.ExampleStringReceiver;
import lv.ctco.tpl.rabbitmq.example.ExampleSender;
import org.junit.Test;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.mockito.LatchCountDownAndCallRealMethodAnswer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;

@Slf4j
public class SendReceiveTest extends IntegrationTest {

    public static final int SECONDS_TO_WAIT_FOR_RESPONSE = 10;

    @Autowired
    ExampleSender sender;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void testSendReceiveAsString() throws Exception {
        String message = UUID.randomUUID().toString();
        ExampleStringReceiver receiver = harness.getSpy(ExampleStringReceiver.ID);
        LatchCountDownAndCallRealMethodAnswer answer = new LatchCountDownAndCallRealMethodAnswer(1);
        doAnswer(answer).when(receiver).onMessage(message);

        log.warn("Sending message");
        sender.send(message);
        log.warn("Message sent, now waiting {} seconds for response to arrive", SECONDS_TO_WAIT_FOR_RESPONSE);

        assertTrue(answer.getLatch().await(SECONDS_TO_WAIT_FOR_RESPONSE, TimeUnit.SECONDS));
    }

    @Test
    public void testSendReceiveAsBean() throws Exception {
        ExampleBean message = ExampleBean.builder().id(UUID.randomUUID().toString()).name(UUID.randomUUID().toString()).build();
        ExampleObjectReceiver receiver = harness.getSpy(ExampleObjectReceiver.ID);
        LatchCountDownAndCallRealMethodAnswer answer = new LatchCountDownAndCallRealMethodAnswer(1);
        doAnswer(answer).when(receiver).onMessage(message);

        log.warn("Sending message");
        sender.sendAsObject(message);
        log.warn("Message sent, now waiting {} seconds for response to arrive", SECONDS_TO_WAIT_FOR_RESPONSE);

        assertTrue(answer.getLatch().await(SECONDS_TO_WAIT_FOR_RESPONSE, TimeUnit.SECONDS));
    }

}
