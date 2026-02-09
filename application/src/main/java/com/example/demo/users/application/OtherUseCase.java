package com.example.demo.users.application;

import com.example.demo.shared.DomainEventPublisher;
import com.example.demo.users.domain.User;
import com.example.demo.users.domain.UserFinder;
import com.example.demo.users.domain.UserId;
import com.example.demo.users.domain.UserRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OtherUseCase {

    private final UserRepository userRepository;
    private final UserFinder finder;
    private final DomainEventPublisher domainEventPublisher;

    public void execute(ChangePasswordCommand command) {
        UserId userId = new UserId(command.getUserId());
        User user = this.finder.find(userId);

        // TODO: mutar agregado raiz

        this.userRepository.save(user);
        this.domainEventPublisher.publish(user.pullEvents());
    }

}
