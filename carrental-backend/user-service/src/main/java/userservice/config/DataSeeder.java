// src/main/java/com/carrental/config/DataSeeder.java
package userservice.config;

import userservice.model.*;
import userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepo;
    private final PasswordEncoder   encoder;   // NoOp

    public DataSeeder(UserRepository userRepo,
                      PasswordEncoder encoder) {
        this.userRepo    = userRepo;
        this.encoder     = encoder;
    }

    @Override
    public void run(String... args) {
        if (userRepo.count() == 0)    seedUsers();
    }

    /* ---------- Seeder-Hilfen ---------- */

    private void seedUsers() {
        List<User> users = List.of(
                new User("Charlie", "Admin", "admin", encoder.encode("admin"), UserRole.ADMIN),
                new User("Alice", "Wonder", "alice", encoder.encode("secret"), UserRole.USER),
                new User("Bob", "Builder", "bob", encoder.encode("secret"), UserRole.TESTER)
        );
        userRepo.saveAll(users);
        LOG.info("👤  {} demo users inserted (NoOp – Klartext)", users.size());
    }
}
