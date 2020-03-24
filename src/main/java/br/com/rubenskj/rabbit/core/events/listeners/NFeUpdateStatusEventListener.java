package br.com.rubenskj.rabbit.core.events.listeners;

import br.com.rubenskj.rabbit.core.events.eventconfiguration.ListenerAdapter;
import br.com.rubenskj.rabbit.core.events.models.NFeUpdateStatusEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NFeUpdateStatusEventListener implements ListenerAdapter<NFeUpdateStatusEvent> {

    @Override
    public void execute(NFeUpdateStatusEvent event) {
        // TODO: continue a cycle of NFe

        log.info("Handling with change status of SeFaz");

        log.debug("NFE -> {}", event.getNFe());
    }
}
