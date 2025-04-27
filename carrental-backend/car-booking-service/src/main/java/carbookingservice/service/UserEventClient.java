package carbookingservice.service;

import carbookingservice.dto.UserDto;
import carbookingservice.dto.UserRequestDto;
import carbookingservice.exception.EntityNotFoundException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventClient {

    @Value("${app.rabbit.exchange}")
    private String exchange;

    @Value("${app.rabbit.routing-key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;
    private final RetryTemplate retryTemplate;

    public UserEventClient(RabbitTemplate rabbitTemplate, RetryTemplate retryTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setReplyTimeout(3000); // 3 seconds timeout
        this.retryTemplate = retryTemplate;
    }

    public UserDto getUserById(String userId) {
        UserRequestDto request = new UserRequestDto(userId);
        System.out.println("Sending message: " + request);

        try {

            return retryTemplate.execute(context -> {
                // Send to the exchange with routing key and get the response
                Object response = rabbitTemplate.convertSendAndReceive(exchange, routingKey, request);

                if (response == null) {
                    // Handle case where no response is received
                    throw new EntityNotFoundException("User", userId);
                }

                // Try casting the response to the expected UserDto
                if (response instanceof UserDto userDto) {
                    return userDto;
                } else {
                    // Handle unexpected response type
                    throw new MessageConversionException("Unexpected response type: " + response.getClass().getName());
                }
            });
        } catch (AmqpException e) {
            // Handle any AMQP-related exceptions (e.g., communication issues)
            System.err.println("Error communicating with RabbitMQ: " + e.getMessage());
            throw new RuntimeException("Failed to communicate with the user service.", e);
        } catch (Exception e) {
            // Catch all other unexpected exceptions
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred while getting user data.", e);
        }
    }
}