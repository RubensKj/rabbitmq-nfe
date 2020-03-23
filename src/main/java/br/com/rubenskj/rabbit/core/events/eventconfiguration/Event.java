package br.com.rubenskj.rabbit.core.events.eventconfiguration;

import org.springframework.context.ApplicationEvent;

public class Event extends ApplicationEvent {
    public Event(Object source) {
        super(source);
    }
}
