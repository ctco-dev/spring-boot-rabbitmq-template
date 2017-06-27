package lv.ctco.tpl.rabbitmq.configuration.sleuth;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SleuthMessageBridge {

    @Autowired
    Tracer tracer;

    public void populateHeaders(Message message, String entryPoint) {
        MessageProperties properties = message.getMessageProperties();
        Span span;
        if (tracer.isTracing()) {
            span = tracer.getCurrentSpan();
        } else {
            span = tracer.createSpan(entryPoint);
        }
        
        properties.setHeader(Span.SPAN_ID_NAME, span.getTraceId());
        properties.setHeader(Span.TRACE_ID_NAME, span.getTraceId());
        properties.setHeader(Span.PROCESS_ID_NAME, span.getProcessId());
        properties.setHeader(Span.SPAN_NAME_NAME, span.getName());
    }

    public Span setupSpan(Message message, String entryPoint) {
        MessageProperties properties = message.getMessageProperties();
        Map<String, Object> headers = properties.getHeaders();
        Span span;
        if (headers.containsKey(Span.TRACE_ID_NAME)) {
            span = Span.builder()
                    .traceId((Long) headers.get(Span.TRACE_ID_NAME))
                    .spanId((Long) headers.get(Span.SPAN_ID_NAME))
                    .name((String) headers.get(Span.SPAN_NAME_NAME))
                    .processId((String) headers.get(Span.PROCESS_ID_NAME))
                    .build();
        } else {
            span = null;
        }
        return tracer.createSpan(entryPoint, span);
    }
}
