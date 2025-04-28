package userservice.config;

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
 * Configuration for RabbitMQ messaging in the User service.
 * <p>
 * Defines the exchange, queue, binding, message converter with class mappings,
 * retry behavior, and RabbitTemplate bean.
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
     * Creates a JSON message converter that maps DTO class names between
     * the CarBookingService and UserService, trusts all packages, and
     * uses inferred types for deserialization.
     *
     * @return a configured Jackson2JsonMessageConverter
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("carbookingservice.dto.UserDto", userservice.dto.UserDto.class);
        idClassMapping.put("carbookingservice.dto.UserRequestDto", userservice.dto.UserRequestDto.class);
        typeMapper.setIdClassMapping(idClassMapping);

        typeMapper.setTrustedPackages("*");
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    /**
     * Declares a topic exchange for routing messages.
     *
     * @return the TopicExchange defined by the application property
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    /**
     * Declares a queue for receiving messages.
     *
     * @return the Queue defined by the application property
     */
    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    /**
     * Binds the queue to the topic exchange using the configured routing key.
     *
     * @param queue    the Queue to bind
     * @param exchange the TopicExchange to bind to
     * @return the created Binding
     */
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    /**
     * Configures a RetryTemplate with a maximum of 3 attempts and an
     * exponential backoff starting at 500 ms, doubling each retry.
     *
     * @return the configured RetryTemplate
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
     * Creates a RabbitTemplate for sending and receiving messages,
     * configured with the connection factory and JSON message converter.
     *
     * @param connectionFactory the RabbitMQ ConnectionFactory
     * @param messageConverter  the Jackson2JsonMessageConverter to use
     * @return a configured RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
