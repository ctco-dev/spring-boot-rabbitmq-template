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

    @Autowired
    ExampleSender sender;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void testSendReceive() throws Exception {
        ExampleReceiver receiver = harness.getSpy(ExampleReceiver.ID);
        LatchCountDownAndCallRealMethodAnswer answer = new LatchCountDownAndCallRealMethodAnswer(1);
        doAnswer(answer).when(receiver).onMessage("hello, world");

        sender.send("hello, world");

        assertTrue(answer.getLatch().await(2, TimeUnit.SECONDS));
    }

}
