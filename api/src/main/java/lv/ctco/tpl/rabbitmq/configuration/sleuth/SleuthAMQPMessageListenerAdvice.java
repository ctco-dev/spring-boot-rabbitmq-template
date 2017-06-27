package lv.ctco.tpl.rabbitmq.configuration.sleuth;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

@Component
public class SleuthAMQPMessageListenerAdvice implements MethodInterceptor {

    @Autowired
    Tracer tracer;
    @Autowired
    SleuthMessageBridge bridge;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Message message = (Message) invocation.getArguments()[1];
        Span span = bridge.setupSpan(message, "receivedMessage");
        try {
            return invocation.proceed();
        } finally {
            tracer.close(span);
        }
    }
}
