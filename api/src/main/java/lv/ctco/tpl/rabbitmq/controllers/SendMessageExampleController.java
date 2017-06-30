package lv.ctco.tpl.rabbitmq.controllers;

import lv.ctco.tpl.rabbitmq.example.ExampleBean;
import lv.ctco.tpl.rabbitmq.example.ExampleSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("example")
public class SendMessageExampleController {

    @Autowired
    ExampleSender sender;

    @RequestMapping("string")
    public String sendHello(@RequestParam("name") String name) {
        sender.send("Hello, " + name);
        return "OK";
    }

    @RequestMapping("object")
    public String sendHelloAsObject(@RequestParam("name") String name) {
        ExampleBean exampleBean = ExampleBean.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .build();
        sender.sendAsObject(exampleBean);
        return "OK";
    }
}
