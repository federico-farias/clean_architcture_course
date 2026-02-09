package com.example.demo.shared;

import java.util.ArrayList;
import java.util.List;

public abstract class RootAggregate {

    private List<DomainEvent> events = new ArrayList<>();

    protected void record(DomainEvent event) {
        this.events.add(event);
    }

    public List<DomainEvent> pullEvents() {
        List<DomainEvent> pulledEvents = this.events;
        this.events = new ArrayList<>();
        return pulledEvents;
    }

}
