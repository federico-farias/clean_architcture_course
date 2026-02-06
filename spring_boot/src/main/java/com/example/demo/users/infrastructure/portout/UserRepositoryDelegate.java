package com.example.demo.users.infrastructure.portout;

import com.example.demo.users.domain.User;
import com.example.demo.users.domain.UserEmail;
import com.example.demo.users.domain.UserName;
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
    public boolean existsByUsername(UserName username) {
        return repository.existsByUsername(username.getValue());
    }

    @Override
    public boolean existsByEmail(UserEmail email) {
        return repository.existsByEmail(email.getValue());
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
               user.getUsername().getValue(),
               user.getEmail().getValue(),
               user.getPassword().getPassword(),
               user.getFirstName().getValue(),
               user.getLastName().getValue()
        );
        UserEntity entitySaved = repository.save(entity);
        //user.setId(entitySaved.getId()); // TODO: cambiar Long id por UUID
        return user;
    }
}
