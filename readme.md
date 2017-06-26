[![Build Status](https://travis-ci.org/ctco-dev/spring-boot-rabbitmq-template.svg?branch=master)](https://travis-ci.org/ctco-dev/spring-boot-rabbitmq-template)

# spring-boot-rabbitmq-template

Template for applications based on Spring Boot and RabbitMQ

## Features

- Default configuration for RabbitMQ
- Integration testing using `spring-rabbit-test` and `qpid` as in-memory AMQP broker

## Required Software

- JDK 1.8

## IDE Setup

### IntelliJ IDEA

#### Lombok

- Download and install Lombok [plugin](https://plugins.jetbrains.com/plugin/6317-lombok-plugin)
- Enable Annotation Processors
  -  Go to Setting->Build, Execution, Deployment-> Compiler->Annotation Processors
  -  Check _Enable annotation processing_

## Develop

2 things are required - running RabbitMQ instance and properly configured `.env` file

### RabbitMQ

Most simple way to get RabbitMQ up and running is to use docker:

`docker run -d -p 5672:5672 -p 15672:15672 rabbitmq`

Or just run it `with docker-compose`:

```
docker-compose up
```

### .env

Ensure that `.env` file has the following content:

```
rabbitmq.host=localhost
rabbitmq.port=5672
rabbitmq.username=guest
rabbitmq.password=guest
rabbitmq.virtual-host=/
rabbitmq.use-ssl=false
```

### Run application

Either run it as command-line application `lv.ctco.tpl.rabbitmq.Application` or run `gradlew bootRun` from command line

### Build

```
gradlew build
```


### Test

```
gradlew test
```

## Technical Stack

- [Spring Boot](https://projects.spring.io/spring-boot/) : Application framework
- [Lombok](https://projectlombok.org/features/index.html) : Utility library for Java language
- [RabbitMQ](https://www.rabbitmq.com/): Message broker
