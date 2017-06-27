package lv.ctco.tpl.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import lv.ctco.tpl.rabbitmq.example.ExampleReceiver;
import lv.ctco.tpl.rabbitmq.example.ExampleSender;
import org.junit.Test;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.mockito.LatchCountDownAndCallRealMethodAnswer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;

@Slf4j
public class SendReceiveTest extends MQTest {

    public static final int SECONDS_TO_WAIT_FOR_RESPONSE = 10;

    @Autowired
    ExampleSender sender;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void testSendReceive() throws Exception {
        ExampleReceiver receiver = harness.getSpy(ExampleReceiver.ID);
        LatchCountDownAndCallRealMethodAnswer answer = new LatchCountDownAndCallRealMethodAnswer(1);
        doAnswer(answer).when(receiver).onMessage("hello, world");

        log.warn("Sending message");
        sender.send("hello, world");
        log.warn("Message sent, now waiting {} seconds for response to arrive", SECONDS_TO_WAIT_FOR_RESPONSE);

        assertTrue(answer.getLatch().await(SECONDS_TO_WAIT_FOR_RESPONSE, TimeUnit.SECONDS));
    }

}
