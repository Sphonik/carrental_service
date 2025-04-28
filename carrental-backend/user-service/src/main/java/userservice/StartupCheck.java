package userservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import userservice.repository.UserRepository;

/**
 * Component that runs on application startup to log the total number of users,
 * verifying database connectivity and initial data seeding.
 */
@Component
public class StartupCheck implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupCheck.class);

    private final UserRepository userRepo;

    /**
     * Constructs a new StartupCheck with the specified UserRepository.
     *
     * @param userRepo repository for accessing User entities
     */
    public StartupCheck(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Executes after the application context is loaded,
     * logging the current count of users.
     *
     * @param args runtime arguments (ignored)
     */
    @Override
    public void run(String... args) {
        log.info("👤  Users    : {}", userRepo.count());
    }
}
