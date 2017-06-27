package lv.ctco.tpl.rabbitmq;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class BootstrapTest extends IntegrationTest {

    @Autowired
    ApplicationContext ctx;

    @Test
    public void shouldStartUpProperly() throws Exception {
        assertThat(ctx, is(notNullValue()));
    }
}
