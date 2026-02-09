package com.example.demo.users.infrastructure.portin;

import com.example.demo.users.domain.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendEmailOnUserCreatedEvent {

    // private final EmailSenderUseCase emailSenderUseCase;

    @EventListener
    public void on(UserCreatedEvent event) {
      log.info("Enviando email de bienvenida a: {}", event.getEmail());
      // this.emailSenderUseCase.sendWelcomeEmail(...
    }

}
