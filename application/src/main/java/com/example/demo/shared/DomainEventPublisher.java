package com.example.demo.shared;

import java.util.List;

public interface DomainEventPublisher {

    void publish(List<DomainEvent> event);

}
