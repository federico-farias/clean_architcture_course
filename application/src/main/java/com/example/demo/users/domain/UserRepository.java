package com.example.demo.users.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    boolean existsByUsername(UserName username);
    
    boolean existsByEmail(UserEmail email);

    void save(User user);

    User find(UserId userId);
}