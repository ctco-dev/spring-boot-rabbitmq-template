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

    public void populateHeaders(Message message) {
        if (!tracer.isTracing()) {
            return;
        }

        MessageProperties properties = message.getMessageProperties();
        Span span = tracer.getCurrentSpan();

        //Based on FeignRequestInjector implementation
        Long parentId = getParentId(span);
        if (parentId != null) {
            properties.setHeader(Span.PARENT_ID_NAME, Span.idToHex(parentId));
        }

        properties.setHeader(Span.SPAN_ID_NAME, Span.idToHex(span.getSpanId()));
        properties.setHeader(Span.TRACE_ID_NAME, span.traceIdString());
        properties.setHeader(Span.PROCESS_ID_NAME, span.getProcessId());
        properties.setHeader(Span.SPAN_NAME_NAME, span.getName());
    }

    private static Long getParentId(Span span) {
        return !span.getParents().isEmpty() ? span.getParents().get(0) : null;
    }

    public Span setupSpan(Message message, String entryPoint) {
        MessageProperties properties = message.getMessageProperties();
        Map<String, Object> headers = properties.getHeaders();
        Span span;
        if (headers.containsKey(Span.TRACE_ID_NAME)) {

            //based on HeaderBasedMessagingExtractor implementation
            String traceId = (String) headers.get(Span.TRACE_ID_NAME);
            Span.SpanBuilder builder = Span.builder();

            Object parentId = headers.get(Span.PARENT_ID_NAME);
            if (parentId != null) {
                builder.parent(Span.hexToId((String) parentId));
            }

            Object processId = headers.get(Span.PROCESS_ID_NAME);
            if (processId != null) {
                builder.processId((String) processId);
            }

            Object spanName = headers.get(Span.SPAN_NAME_NAME);
            if (spanName != null) {
                builder.name((String) spanName);
            }

            span = builder
                    .traceId(Span.hexToId(traceId))
                    .traceIdHigh(traceId.length() == 32 ? Span.hexToId(traceId, 0) : 0)
                    .spanId(Span.hexToId((String) headers.get(Span.SPAN_ID_NAME)))
                    .build();
        } else {
            span = null;
        }
        return tracer.createSpan(entryPoint, span);
    }
}
