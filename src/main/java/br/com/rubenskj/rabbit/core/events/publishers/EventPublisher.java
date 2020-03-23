package br.com.rubenskj.rabbit.core.events.publishers;

import br.com.rubenskj.rabbit.core.events.eventconfiguration.Event;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public EventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(Event event) {
        this.applicationEventPublisher.publishEvent(event);
    }
}
