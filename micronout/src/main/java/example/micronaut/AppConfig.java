package example.micronaut;

import com.example.fede.demo.application.UserService;
import example.micronaut.portout.MemoryUserRepository;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class AppConfig {

    @Singleton
    public UserService userService(MemoryUserRepository repositoryInMemory) {
        return new UserService(repositoryInMemory);
    }

}
