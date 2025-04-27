package userservice.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import userservice.service.UserEventPublisher;

@RestController
@RequestMapping("/users")
public class UserRabbitController {

    private final UserEventPublisher publisher;

    public UserRabbitController(UserEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/{id}")
    public String createUser(@PathVariable String id) {
        System.out.println("🔥 Controller hit with user ID: " + id);
        publisher.publishUserCreated(id);
        return "User created!";
    }
}
