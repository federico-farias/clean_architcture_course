package com.example.demo.users.infrastructure.portin;

import com.example.demo.users.domain.UserPasswordChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendEmailOnPasswordChangedEvent {

    @EventListener
    public void on(UserPasswordChangedEvent event) {
      log.info("Enviando email de cambio de contraseña a: {}", event.getEmail());
    }

}
