package example.micronaut.portout;

import com.example.fede.demo.application.User;
import com.example.fede.demo.application.UserRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class MemoryUserRepository implements UserRepository {

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public boolean existsByUsername(String username) {
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public List<User> findAllActiveUsers() {
        return List.of();
    }

    @Override
    public List<User> findByName(String name) {
        return List.of();
    }

    @Override
    public User save(User user) {
        return null;
    }
}
