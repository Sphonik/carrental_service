package carbookingservice.component;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Spring component that listens for user-related events on a RabbitMQ queue
 * and processes incoming messages.
 */
@Component
public class UserEventListener {

    /**
     * Receives and handles a raw message payload from the configured RabbitMQ queue.
     * <p>
     * The queue name is injected from the application property
     * <code>app.rabbit.queue</code>.
     *
     * @param message the raw JSON or text message sent by the publisher
     */
    @RabbitListener(queues = "${app.rabbit.queue}")
    public void handleUserEvent(String message) {
        System.out.println("Received message: " + message);
    }
}
