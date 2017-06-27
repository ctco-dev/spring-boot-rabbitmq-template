package lv.ctco.tpl.rabbitmq.configuration.sleuth;

import lv.ctco.tpl.rabbitmq.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class SleuthAMQPMessagePostProcessorTest extends IntegrationTest {

    @Autowired
    RabbitTemplate template;
    @Autowired
    Tracer tracer;

    @Before
    public void ensureNoSpan() throws Exception {
        tracer.close(tracer.getCurrentSpan());
        assertThat(tracer.getCurrentSpan(), is(nullValue()));
    }

    @Test
    public void testPopulate_ExistingSpan() throws Exception {
        Span root = tracer.createSpan("root");
        Message message = new Message("hello".getBytes(), new MessageProperties());
        template.send(message);
        assertThat(message.getMessageProperties().getHeaders().get(Span.TRACE_ID_NAME), is(root.getTraceId()));
        assertThat(message.getMessageProperties().getHeaders().get(Span.SPAN_ID_NAME), is(root.getSpanId()));
        assertThat(message.getMessageProperties().getHeaders().get(Span.SPAN_NAME_NAME), is(root.getName()));

    }

    @Test
    public void testPopulate_NewSpan() throws Exception {
        Message message = new Message("hello".getBytes(), new MessageProperties());
        template.send(message);
        assertThat(message.getMessageProperties().getHeaders().get(Span.TRACE_ID_NAME), is(notNullValue()));
        assertThat(message.getMessageProperties().getHeaders().get(Span.SPAN_ID_NAME), is(notNullValue()));
        assertThat(message.getMessageProperties().getHeaders().get(Span.SPAN_NAME_NAME), is(notNullValue()));

    }

}