package userservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import userservice.dto.UserDto;
import userservice.dto.UserRequestDto;

@Service
public class UserEventListener {
    private final UserService userService;

    public UserEventListener(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = "user.queue")
    public UserDto handleUserRequest(UserRequestDto request) {
        try {
            System.out.println("📨 Received RPC request for user " + request);
            return userService.getUserDto(request.id());
        } catch (Exception e) {
            System.err.println("❌ Error in listener: " + e.getMessage());
            e.printStackTrace();
            throw e; // Rethrow to let Spring's error handler deal with it
        }
    }
}