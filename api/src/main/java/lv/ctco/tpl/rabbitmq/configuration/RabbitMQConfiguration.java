package lv.ctco.tpl.rabbitmq.configuration;

import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
@Slf4j
public class RabbitMQConfiguration {

    public static final String QUEUE_NAME = "queue";
    public static final String EXCHANGE_NAME = "exchange";


    @Value("${rabbitmq.host}")
    String hostname;
    @Value("${rabbitmq.port}")
    int port;
    @Value("${rabbitmq.username}")
    String userName;
    @Value("${rabbitmq.password}")
    String password;
    @Value("${rabbitmq.virtual-host:/}")
    String virtualHost;
    @Value("${rabbitmq.use-ssl:false}")
    boolean useSSL;

    @Bean
    CachingConnectionFactory connectionFactory() {
        log.info("Creating connection factory for MQ broker...");
        ConnectionFactory cf = new ConnectionFactory();
        cf.setHost(hostname);
        cf.setPort(port);
        cf.setUsername(userName);
        cf.setPassword(password);
        cf.setVirtualHost(virtualHost);
        cf.setAutomaticRecoveryEnabled(false);
        if (useSSL) {
            try {
                cf.useSslProtocol();
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                //TODO don't throw raw exceptions
                throw new RuntimeException(e);
            }
        }

        log.info("Connection factory created.");
        return new CachingConnectionFactory(cf);
    }

    @Bean
    RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory  cf) {
        RabbitTemplate template = new RabbitTemplate(cf);
        RetryTemplate retry = new RetryTemplate();
        ExponentialBackOffPolicy backOff = new ExponentialBackOffPolicy();
        backOff.setInitialInterval(1000);
        backOff.setMultiplier(1.5);
        backOff.setMaxInterval(60000);
        retry.setBackOffPolicy(backOff);
        template.setRetryTemplate(retry);
        return template;
    }

    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_NAME);
    }

}
