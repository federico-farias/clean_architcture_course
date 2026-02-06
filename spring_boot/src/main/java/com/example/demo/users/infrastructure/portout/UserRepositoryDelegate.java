package com.example.demo.users.infrastructure.portout;

import com.example.demo.users.domain.User;
import com.example.demo.users.domain.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserRepositoryDelegate implements UserRepository {

    private final UserRepositorySpringData repository;

    public UserRepositoryDelegate(UserRepositorySpringData repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public List<User> findAllActiveUsers() {
        return repository.findAllActiveUsers();
    }

    @Override
    public List<User> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity(
               user.getUsername(),
               user.getEmail(),
               user.getPassword(),
               user.getFirstName(),
               user.getLastName()
        );
        UserEntity entitySaved = repository.save(entity);
        user.setId(entitySaved.getId());
        return user;
    }
}
