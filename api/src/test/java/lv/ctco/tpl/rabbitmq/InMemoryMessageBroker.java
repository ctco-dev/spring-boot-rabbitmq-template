package lv.ctco.tpl.rabbitmq;

import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;

public class InMemoryMessageBroker {

    private final Broker broker = new Broker();

    BrokerOptions brokerOptions() {
        BrokerOptions brokerOptions = new BrokerOptions();

        brokerOptions.setConfigProperty("qpid.amqp_port", String.valueOf(IntegrationTest.PORT));
        brokerOptions.setInitialConfigurationLocation(getClass().getResource("/initial-config.json").getFile());
        brokerOptions.setStartupLoggedToSystemOut(true);

        return brokerOptions;
    }

    public void start() throws Exception {
        broker.startup(brokerOptions());
    }

    public void stop() {
        broker.shutdown();
    }
}
