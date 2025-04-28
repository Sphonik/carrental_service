package carbookingservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for RabbitMQ messaging infrastructure, including
 * exchange, queue, binding, message converter, retry behavior, and template.
 */
@Configuration
public class RabbitConfig {

    @Value("${app.rabbit.exchange}")
    private String exchange;

    @Value("${app.rabbit.queue}")
    private String queue;

    @Value("${app.rabbit.routing-key}")
    private String routingKey;

    /**
     * Configures a JSON message converter that:
     * <ul>
     *   <li>Maps DTO class names between microservices via a custom type mapping.</li>
     *   <li>Trusts all packages for deserialization.</li>
     *   <li>Uses inferred types for message payloads.</li>
     * </ul>
     *
     * @return a Jackson2JsonMessageConverter with custom type mapping
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("userservice.dto.UserDto", carbookingservice.dto.UserDto.class);
        idClassMapping.put("userservice.dto.UserRequestDto", carbookingservice.dto.UserRequestDto.class);
        typeMapper.setIdClassMapping(idClassMapping);

        typeMapper.setTrustedPackages("*");
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    /**
     * Declares a topic exchange for publishing and routing messages.
     *
     * @return a TopicExchange named according to the application property
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    /**
     * Declares a queue to receive messages.
     *
     * @return a Queue named according to the application property
     */
    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    /**
     * Binds the queue to the topic exchange using the configured routing key.
     *
     * @param queue    the queue to bind
     * @param exchange the exchange to bind to
     * @return the resulting Binding
     */
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    /**
     * Configures a retry template with:
     * <ul>
     *   <li>Simple retry policy: maximum of 3 attempts.</li>
     *   <li>Exponential backoff policy: initial interval of 500 ms, doubling each retry.</li>
     * </ul>
     *
     * @return a RetryTemplate for use in message publishing or processing
     */
    @Bean
    public RetryTemplate retryTemplate() {
        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(3);

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMultiplier(2);

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(policy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }

    /**
     * Creates a RabbitTemplate configured with the given connection factory
     * and JSON message converter for sending and receiving messages.
     *
     * @param connectionFactory the RabbitMQ connection factory
     * @param messageConverter  the JSON message converter
     * @return a RabbitTemplate for messaging operations
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
