package userservice.config;

import userservice.model.User;
import userservice.model.UserRole;
import userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component that seeds demo users into the repository on application startup
 * if no users are present.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    /**
     * Constructs the DataSeeder with the required repository and password encoder.
     *
     * @param userRepo repository for persisting User entities
     * @param encoder  password encoder used to hash user passwords
     */
    public DataSeeder(UserRepository userRepo,
                      PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder  = encoder;
    }

    /**
     * Runs after application startup and seeds users if the repository is empty.
     *
     * @param args runtime arguments (ignored)
     */
    @Override
    public void run(String... args) {
        if (userRepo.count() == 0) {
            seedUsers();
        }
    }

    /**
     * Inserts a predefined set of demo users with encoded passwords and roles.
     */
    private void seedUsers() {
        List<User> users = List.of(
                new User("Charlie", "Admin", "admin", encoder.encode("admin"), UserRole.ADMIN),
                new User("Alice",   "Wonder", "alice", encoder.encode("secret"), UserRole.USER),
                new User("Bob",     "Builder","bob",   encoder.encode("secret"), UserRole.TESTER)
        );
        userRepo.saveAll(users);
        LOG.info("👤  {} demo users inserted", users.size());
    }
}
