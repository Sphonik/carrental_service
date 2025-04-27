package userservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbit.exchange}")
    private String exchange;

    @Value("${app.rabbit.routing-key}")
    private String routingKey;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        testConnection();
    }

    private void testConnection() {
        try {
            rabbitTemplate.execute(channel -> {
                System.out.println("🐇 RabbitMQ is connected!");
                return null;
            });
        } catch (Exception e) {
            System.out.println("🚨 Failed to connect to RabbitMQ: " + e.getMessage());
        }
    }

    public void publishUserCreated(String userId) {
        String message = "User created: " + userId;
        System.out.println("📤 Sending message: " + message);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
