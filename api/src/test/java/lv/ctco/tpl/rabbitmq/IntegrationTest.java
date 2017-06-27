package lv.ctco.tpl.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(properties = {
        "rabbitmq.host=localhost",
        "rabbitmq.port=" + IntegrationTest.PORT,
        "rabbitmq.virtual-host=default",
        "rabbitmq.username=guest",
        "rabbitmq.password=guest"
})
@RunWith(SpringRunner.class)
@Slf4j
public abstract class IntegrationTest {

    public static final int PORT = 5672;

    private static final InMemoryMessageBroker broker = new InMemoryMessageBroker();

    @BeforeClass
    public static void setUp() throws Exception {
        log.warn("Starting broker");
        broker.start();
        log.warn("Broker started");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        log.warn("Stopping broker");
        broker.stop();
        log.warn("Broker stopped");
    }

}
