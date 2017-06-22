package lv.ctco.tpl.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@Slf4j
public class Application extends SpringBootServletInitializer {

    static Stream<String> getEnvProperties() throws IOException {
        File file = new File(".env");
        if (file.exists()) {
            log.info("Found .env file, taking additional properties from it");
            Properties properties = new Properties();
            properties.load(new FileReader(file));
            return properties.entrySet().
                    stream().
                    filter((entry) -> !StringUtils.isEmpty(entry.getValue())).
                    map((entry) -> "--" + entry.getKey() + "=" + entry.getValue());
        } else {
            log.info("No .env file provided");
            return Stream.empty();
        }
    }

    static Stream<String> getArgs(String[] cliArgs) throws IOException {
        return Stream.concat(Stream.of(cliArgs), getEnvProperties());
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class,
                getArgs(args).
                        collect(Collectors.toList()).
                        toArray(new String[] {})
        );
    }
}
