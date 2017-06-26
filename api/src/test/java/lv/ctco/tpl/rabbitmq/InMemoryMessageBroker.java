package lv.ctco.tpl.rabbitmq;

import com.google.common.io.Files;
import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;

import java.io.File;

public class InMemoryMessageBroker {

    private final Broker broker = new Broker();

    BrokerOptions brokerOptions() {
        File tmpFolder = Files.createTempDir();

        BrokerOptions brokerOptions = new BrokerOptions();

        brokerOptions.setConfigProperty("qpid.work_dir", tmpFolder.getAbsolutePath());
        brokerOptions.setConfigProperty("qpid.amqp_port", String.valueOf(MQTest.PORT));
        brokerOptions.setInitialConfigurationLocation(getClass().getResource("/initial-config.json").getFile());
        brokerOptions.setStartupLoggedToSystemOut(false);

        return brokerOptions;
    }

    public void start() throws Exception {
        broker.startup(brokerOptions());
    }

    public void stop() {
        broker.shutdown();
    }
}
