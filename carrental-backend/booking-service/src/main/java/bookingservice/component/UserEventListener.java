package bookingservice.component;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    @RabbitListener(queues = "${app.rabbit.queue}")
    public void handleUserEvent(String message) {
        System.out.println("Received message: " + message);
    }
}
