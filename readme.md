# Running locally

## RabbitMQ

Most simple way to get RabbitMQ up and running is to use docker:

`docker run -d -p 5672:5672 -p 15672:15672 rabbitmq`

Or just run it `with docker-compose`:

```
docker-compose up
```

## .env

Ensure that `.env` file has the following content:

```
rabbitmq.host=localhost
rabbitmq.port=5672
rabbitmq.username=guest
rabbitmq.password=guest
rabbitmq.virtual-host=/
rabbitmq.use-ssl=false
```