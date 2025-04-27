package userservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import userservice.repository.UserRepository;

@Component
public class StartupCheck implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupCheck.class);

    private final UserRepository userRepo;

    public StartupCheck(UserRepository userRepo) {
        this.userRepo     = userRepo;
    }

    @Override
    public void run(String... args) {
        log.info("👤  Users    : {}", userRepo.count());
    }
}
