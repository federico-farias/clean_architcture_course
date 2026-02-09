package com.example.demo.users.infrastructure.portout;

import com.example.demo.users.domain.User;
import com.example.demo.users.domain.UserEmail;
import com.example.demo.users.domain.UserName;
import com.example.demo.users.domain.UserRepository;


public class UserRepositoryDelegate implements UserRepository {

    private final UserRepositorySpringData repository;

    public UserRepositoryDelegate(UserRepositorySpringData repository) {
        this.repository = repository;
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
    public void save(User user) {
        UserEntity entity = new UserEntity(
               user.getUsername().getValue(),
               user.getEmail().getValue(),
               user.getPassword().getValue(),
               user.getFirstName().getValue(),
               user.getLastName().getValue()
        );
        repository.save(entity);
    }
}
