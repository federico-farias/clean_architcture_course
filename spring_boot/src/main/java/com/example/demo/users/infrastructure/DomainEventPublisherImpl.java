package com.example.demo.users.infrastructure;

import com.example.demo.shared.DomainEvent;
import com.example.demo.shared.DomainEventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DomainEventPublisherImpl implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(List<DomainEvent> events) {
        events.forEach(this.applicationEventPublisher::publishEvent);
    }

}

