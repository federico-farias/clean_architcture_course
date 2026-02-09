package com.example.fede.demo.application;

import com.example.demo.users.domain.User;
import com.example.demo.users.domain.UserEmail;
import com.example.demo.users.domain.UserName;
import com.example.demo.users.domain.UserRepository;

import java.util.List;
import java.util.Optional;

public abstract class InMemoryStubUserRepository implements UserRepository {

    protected int saveCounter;
    protected int existsByEmailCounter;
    protected int existsByUsernameCounter;

    public void existsByUsernameVerifier(int times) {
        if (this.existsByUsernameCounter != times) {
            throw new AssertionError("Expected existsByUsername to be called " + times + " times, but was called " + this.existsByUsernameCounter + " times.");
        }
    }

    @Override
    public boolean existsByUsername(UserName username) {
        return false;
    }

    @Override
    public boolean existsByEmail(UserEmail email) {
        return false;
    }

    @Override
    public void save(User user) {
    }

    public void existsByEmailVerifier(int times) {
        if (this.existsByUsernameCounter != times) {
            throw new AssertionError("Expected existsByEmail to be called " + times + " times, but was called " + this.existsByEmailCounter + " times.");
        }
    }

    public void saveVerify(int times) {
        if (this.saveCounter != times) {
            throw new AssertionError("Expected save to be called " + times + " times, but was called " + this.saveCounter + " times.");
        }
    }
}
