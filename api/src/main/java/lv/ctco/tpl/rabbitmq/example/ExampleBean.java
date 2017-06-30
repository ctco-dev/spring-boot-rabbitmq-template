package lv.ctco.tpl.rabbitmq.example;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@ToString
@EqualsAndHashCode
public class ExampleBean {
    private String id;
    private String name;
}
