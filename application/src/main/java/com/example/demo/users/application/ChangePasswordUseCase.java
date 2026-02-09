package com.example.demo.users.application;

import com.example.demo.shared.DomainEventPublisher;
import com.example.demo.users.domain.User;
import com.example.demo.users.domain.UserFinder;
import com.example.demo.users.domain.UserId;
import com.example.demo.users.domain.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final UserFinder finder;
    private final DomainEventPublisher domainEventPublisher;

    public void change(ChangePasswordCommand command) {
        UserId userId = new UserId(command.getUserId());
        User user = this.finder.find(userId);

        user.changePassword(command.getCurrentPassword(), command.getNewPassword());

        this.userRepository.save(user);
        this.domainEventPublisher.publish(user.pullEvents());
    }

}
