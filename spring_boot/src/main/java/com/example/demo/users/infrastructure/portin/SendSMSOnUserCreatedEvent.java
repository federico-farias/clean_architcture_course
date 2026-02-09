package com.example.demo.users.infrastructure.portin;

import com.example.demo.users.domain.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendSMSOnUserCreatedEvent {

    @EventListener
    public void on(UserCreatedEvent event) {
      log.info("Send SMS to: {}", event.getEmail());
    }

}
