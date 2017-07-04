package lv.ctco.tpl.rabbitmq.configuration.sleuth;

import lv.ctco.tpl.rabbitmq.IntegrationTest;
import lv.ctco.tpl.rabbitmq.example.ExampleRoutingKeys;
import lv.ctco.tpl.rabbitmq.example.ExampleStringReceiver;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;

public class SleuthAMQPMessageListenerAdviceTest extends IntegrationTest {

    @Autowired
    RabbitTemplate template;
    @Autowired
    Tracer tracer;

    @Autowired
    RabbitListenerTestHarness harness;

    @Test
    public void testExtractTraceId_currentSpan() throws Exception {
        String messageContent = UUID.randomUUID().toString();
        ExampleStringReceiver receiver = harness.getSpy(ExampleStringReceiver.ID);
        ValueAnswer<Span> answer = new ValueAnswer<>(() -> tracer.getCurrentSpan());
        doAnswer(answer).when(receiver).onMessage(anyString());

        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN).build();

        Message message = new Message(messageContent.getBytes(), properties);
        tracer.createSpan("root");
        template.send(ExampleRoutingKeys.AS_STRING, message);
        tracer.close(tracer.getCurrentSpan());

        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        String traceIdString = (String) headers.get(Span.TRACE_ID_NAME);
        long spanId = Span.hexToId((String) headers.get(Span.SPAN_ID_NAME));

        Span span = answer.getValue().poll(10, TimeUnit.SECONDS);
        assertThat(span, is(notNullValue()));
        assertThat(span.traceIdString(), is(traceIdString));
        assertThat(span.getSpanId(), is(not(spanId)));
        assertThat(span.getParents().get(0), is(spanId));
    }

    private static class ValueAnswer<X> implements Answer {

        private Supplier<X> supplier;
        private BlockingQueue<X> queue;

        public ValueAnswer(Supplier<X> supplier) {
            this.supplier = supplier;
            this.queue = new ArrayBlockingQueue<X>(1);
        }

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            Object result = invocation.callRealMethod();
            this.queue.offer(supplier.get());
            return result;
        }

        public BlockingQueue<X> getValue() {
            return queue;
        }
    }

}