package userservice.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserEventPublisher {

    private final AmqpTemplate amqpTemplate;

    @Value("${app.rabbit.exchange}")
    private String exchange;

    @Value("${app.rabbit.routing-key}")
    private String routingKey;

    public UserEventPublisher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void publishUserCreated(String userId) {
        String message = "User created: " + userId;
        System.out.println("Sending message: " + message);
        amqpTemplate.convertAndSend(exchange, routingKey, message);
    }

    public void publishUserUpdated(String userId) {
        String message = "User updated: " + userId;
        System.out.println("Sending message: " + message);
        amqpTemplate.convertAndSend(exchange, routingKey, message);
    }

    public void publishUserDeleted(String userId) {
        String message = "User deleted: " + userId;
        System.out.println("Sending message: " + message);
        amqpTemplate.convertAndSend(exchange, routingKey, message);
    }
}
