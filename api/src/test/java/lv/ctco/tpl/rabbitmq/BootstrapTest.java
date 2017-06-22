package lv.ctco.tpl.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@SpringBootTest(properties = {
        "rabbitmq.host=localhost",
        "rabbitmq.port=5672",
        "rabbitmq.username=guest",
        "rabbitmq.password=guest"
})
@RunWith(SpringRunner.class)
public class BootstrapTest {

    @Autowired
    ApplicationContext ctx;

    @Test
    public void shouldStartUpProperly() throws Exception {
        assertThat(ctx, is(notNullValue()));
    }
}
