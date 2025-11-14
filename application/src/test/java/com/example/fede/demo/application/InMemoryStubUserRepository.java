package com.example.fede.demo.application;

import java.util.List;
import java.util.Optional;

public abstract class InMemoryStubUserRepository implements UserRepository {

    protected int saveCounter;
    protected int existsByEmailCounter;
    protected int existsByUsernameCounter;

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

    public void existsByUsernameVerifier(int times) {
        if (this.existsByUsernameCounter != times) {
            throw new AssertionError("Expected existsByUsername to be called " + times + " times, but was called " + this.existsByUsernameCounter + " times.");
        }
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
