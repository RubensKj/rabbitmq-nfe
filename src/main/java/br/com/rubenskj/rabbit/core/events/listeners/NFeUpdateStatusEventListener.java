package br.com.rubenskj.rabbit.core.events.listeners;

import br.com.rubenskj.rabbit.core.events.eventconfiguration.ListenerAdapter;
import br.com.rubenskj.rabbit.core.events.models.NFeReceivedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NFeUpdateStatusEventListener implements ListenerAdapter<NFeReceivedEvent> {

    @Override
    public void execute(NFeReceivedEvent event) throws Exception {
        // TODO: continue a cycle of NFe

        log.info("Handling with change status of SeFaz");
    }
}
